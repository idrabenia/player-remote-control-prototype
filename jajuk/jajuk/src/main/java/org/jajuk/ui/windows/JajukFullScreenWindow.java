/*
 *  Jajuk
 *  Copyright (C) The Jajuk Team
 *  http://jajuk.info
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  
 */
package org.jajuk.ui.windows;

import static org.jajuk.ui.actions.JajukActions.NEXT_TRACK;
import static org.jajuk.ui.actions.JajukActions.PAUSE_RESUME_TRACK;
import static org.jajuk.ui.actions.JajukActions.PREVIOUS_TRACK;
import static org.jajuk.ui.actions.JajukActions.STOP_TRACK;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

import org.jajuk.ui.actions.ActionManager;
import org.jajuk.ui.actions.JajukActions;
import org.jajuk.ui.helpers.JajukMouseAdapter;
import org.jajuk.ui.substance.CircleButtonShaper;
import org.jajuk.ui.substance.LeftConcaveButtonShaper;
import org.jajuk.ui.substance.RightConcaveButtonShaper;
import org.jajuk.ui.substance.RoundRectButtonShaper;
import org.jajuk.ui.views.AnimationView;
import org.jajuk.ui.views.CoverView;
import org.jajuk.ui.widgets.JajukButton;
import org.jajuk.ui.widgets.TrackPositionSliderToolbar;
import org.jajuk.util.IconLoader;
import org.jajuk.util.JajukIcons;
import org.jajuk.util.UtilGUI;
import org.jajuk.util.UtilSystem;
import org.jajuk.util.log.Log;
import org.jvnet.substance.SubstanceLookAndFeel;

/**
 * The full screen window Note that not all operating support full screen mode.
 * If the OS doesn't support it, the user cannot access to it so we have not to
 * handle any errors.
 */
public class JajukFullScreenWindow extends JWindow implements IJajukWindow {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = -2859302706462954993L;
  private static JajukFullScreenWindow instance = null;
  private GraphicsDevice graphicsDevice;
  private JButton jbPrevious;
  private JButton jbNext;
  private JButton jbPlayPause;
  private JButton jbStop;
  private JajukButton jbFull;
  private JajukButton jbExit;
  private CoverView coverView;
  /** State decorator. */
  private WindowStateDecorator decorator;
  private AnimationView animationView;
  private JPanel jtbPlay;
  private Timer hideMouseTimer;
  /** Owning frame, see bellow for explanations *. */
  private static JFrame owner;
  /**
   * See http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html We have
   * to use a frame owner of this jwindow to allow full screen mode to respond
   * to key events
   */
  static {
    owner = new JFrame();
    owner.setSize(new Dimension(0, 0));
    owner.setUndecorated(true);
    owner.requestFocus();
    // Add escape listening to exit full-screen mode
    owner.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          try {
            ActionManager.getAction(JajukActions.FULLSCREEN_JAJUK).perform(null);
          } catch (Exception e1) {
            Log.error(e1);
          }
        }
      }
    });
    // Install global keystrokes
    WindowGlobalKeystrokeManager.getInstance();
  }

  /**
   * Return whether we should try to switch to fullscreen mode (and lose keystrokes controls).
   *
   * @return whether we should try to switch to fullscreen mode
   */
  private static boolean shouldTryNativeFullScreenmode() {
    return
    // Check full screen mode is actually supported
    (instance.graphicsDevice.isFullScreenSupported() &&
    // Use real full screen mode only under OSX to override the task bar, otherwise, 
    // we only maximize the frame to make keystrokes working.
    UtilSystem.isUnderOSX());
  }

  /**
   * Gets the single instance of JajukFullScreenWindow.
   * 
   * @return single instance of JajukFullScreenWindow
   */
  public static synchronized JajukFullScreenWindow getInstance() {
    if (instance == null) {
      instance = new JajukFullScreenWindow();
      instance.decorator = new WindowStateDecorator(instance) {
        @Override
        public void specificBeforeShown() {
          instance.graphicsDevice = UtilGUI.getGraphicsDeviceOfMainFrame();
          if (shouldTryNativeFullScreenmode()) {
            instance.graphicsDevice.setFullScreenWindow(instance);
          }
        }

        @Override
        public void specificAfterShown() {
          if (!shouldTryNativeFullScreenmode()) {
            Dimension dimension = null;
            Point point = null;
            // Probably due to JRE bug #6921661, instance.graphicsDevice.getDisplayMode() sometimes 
            // returns null under some OS like Fedora as a VMWare guest OS. Then, we can enter properly 
            // into fullscreen mode but we we cannot leave it anymore 
            // (java.lang.IllegalArgumentException: Invalid display mode ).
            // So we try to detect defunct configurations and we use Toolkit (dual screen unaware)
            // To find single screen size.
            if (instance.graphicsDevice.getDisplayMode() != null) {
              // No full screen mode but no JRE bug
              dimension = new Dimension(instance.graphicsDevice.getDisplayMode().getWidth(),
                  instance.graphicsDevice.getDisplayMode().getHeight());
              point = new Point(instance.graphicsDevice.getDefaultConfiguration().getBounds()
                  .getLocation());
            } else {
              // No full screen mode AND JRE bug
              dimension = Toolkit.getDefaultToolkit().getScreenSize();
              point = new Point(0, 0);
            }
            instance.setSize(dimension);
            instance.setLocation(point);
            owner.setVisible(true);
            owner.requestFocus();
          }
        }

        @Override
        public void specificAfterHidden() {
          if (shouldTryNativeFullScreenmode()) {
            instance.graphicsDevice.setFullScreenWindow(null);
          }
          owner.setVisible(false);
          instance.dispose();
        }

        @Override
        public void specificBeforeHidden() {
          if (instance.graphicsDevice.isFullScreenSupported()) {
            // Make sure to reset everything like it was before entering fullscreen mode
            // (even if it wasn't the case)
            instance.graphicsDevice.setFullScreenWindow(null);
          }
        }
      };
    }
    return instance;
  }

  /**
   * Hide mouse timer.
   * */
  private void hideMouseTimer() {
    setCursor(Cursor.getDefaultCursor());
    if (hideMouseTimer != null) {
      hideMouseTimer.restart();
    } else {
      initMouseTimer();
    }
  }

  /**
   * Inits the mouse timer.
   * */
  private void initMouseTimer() {
    hideMouseTimer = new Timer(3000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
            IconLoader.getIcon(JajukIcons.NONE).getImage(), new Point(0, 0), "invisibleCursor"));
      }
    });
    hideMouseTimer.start();
  }

  /**
   * Instantiates a new jajuk full screen window.
   */
  public JajukFullScreenWindow() {
    super();
    setAlwaysOnTop(true);
    // Add Mouse Listener to disable mouse cursor
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        hideMouseTimer();
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        hideMouseTimer();
      }
    });
    // activate Timer
    hideMouseTimer();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jajuk.ui.windows.IJajukWindow#initUI()
   */
  @Override
  public void initUI() {
    // Full screen switch button
    jbFull = new JajukButton(ActionManager.getAction(JajukActions.FULLSCREEN_JAJUK));
    // Exit button
    jbExit = new JajukButton(ActionManager.getAction(JajukActions.EXIT));
    // Animation view
    animationView = new AnimationView();
    animationView.initUI();
    // Cover view
    coverView = new CoverView();
    coverView.initUI(false);
    // Player toolbar
    jtbPlay = getPlayerPanel();
    // Information panel
    TrackPositionSliderToolbar tpst = new TrackPositionSliderToolbar();
    // Add items
    setLayout(new MigLayout("ins 0,gap 0 0", "[grow]", "[][grow][70%!][][]"));
    add(jbFull, "right,split 2,gapright 5");
    add(jbExit, "right,wrap");
    add(animationView, "alignx center,aligny top,grow,wrap");
    add(coverView, "alignx center, grow,gap bottom 20,wrap");
    add(jtbPlay, "alignx center,gap bottom 20,wrap");
    add(tpst, "alignx center,width 50%!,aligny bottom,gap bottom 10");
    // Add a contextual menu to leave full screen mode or quit, see also
    // issue #1492
    // TODO : For some reasons, the popup doesn't appears over the cover pic,
    // I have no idea of the reason so far.
    final JPopupMenu popup = new JPopupMenu();
    popup.add(ActionManager.getAction(JajukActions.FULLSCREEN_JAJUK));
    popup.add(ActionManager.getAction(JajukActions.EXIT));
    addMouseListener(new JajukMouseAdapter() {
      @Override
      public void handlePopup(MouseEvent e) {
        popup.show(e.getComponent(), e.getX(), e.getY());
      }

      @Override
      public void handleAction(MouseEvent e) {
        // Void on purpose
      }
    });
  }

  /**
   * Gets the player panel.
   * 
   * @return the player panel
   */
  private JPanel getPlayerPanel() {
    JPanel jPanelPlay = new JPanel();
    jPanelPlay.setLayout(new MigLayout("insets 5", "[grow][grow][grow]"));
    // previous
    jbPrevious = new JajukButton(ActionManager.getAction(PREVIOUS_TRACK));
    int concavity = IconLoader.getIcon(JajukIcons.PLAYER_PLAY).getIconHeight();
    jbPrevious.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY,
        new RightConcaveButtonShaper(concavity));
    jbPrevious.setBorderPainted(true);
    jbPrevious.setContentAreaFilled(true);
    jbPrevious.setFocusPainted(true);
    jbPrevious.addMouseListener(new JajukMouseAdapter() {
      @Override
      public void handlePopup(final MouseEvent me) {
        // Create an ActionEvent from this MouseEvent with a custom modifier : the right click
        ActionEvent ae = new ActionEvent(jbPrevious, 0, PREVIOUS_TRACK.name(), 4332424);
        try {
          ActionManager.getAction(PREVIOUS_TRACK).perform(ae);
        } catch (Exception e) {
          Log.error(e);
        }
      }
    });
    // next
    jbNext = new JajukButton(ActionManager.getAction(NEXT_TRACK));
    jbNext.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY,
        new LeftConcaveButtonShaper(concavity));
    // play pause
    jbPlayPause = new JajukButton(ActionManager.getAction(PAUSE_RESUME_TRACK));
    jbPlayPause.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY,
        new CircleButtonShaper());
    // stop
    jbStop = new JajukButton(ActionManager.getAction(STOP_TRACK));
    jbStop.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY,
        new RoundRectButtonShaper());
    jPanelPlay.add(jbStop, "center,split 6,width 40!,height 30,gapright 5!");
    jPanelPlay.add(jbPrevious, "center,width 62!,height 30!,gapright 0");
    jPanelPlay.add(jbPlayPause, "center,width 45!,height 45!,gapright 0");
    jPanelPlay.add(jbNext, "center,width 62!,height 30!,gapright 3");
    return jPanelPlay;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jajuk.ui.widgets.JajukWindow#getWindowStateDecorator()
   */
  @Override
  public WindowStateDecorator getWindowStateDecorator() {
    return decorator;
  }
}
