/*
 * Scrolling text component Code found at
 * http://www.developpez.net/forums/archive/index.php/t-41622.html Thanks
 * "herve91"
 */
package ext;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * .
 */
public class JScrollingText extends JLabel {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 3068213731703270035L;
  private final int speed;
  private final int period;
  private final int offset;
  private int x = 300;

  /**
   * Instantiates a new j scrolling text.
   * 
   * @param text 
   */
  public JScrollingText(String text) {
    this(text, 1);
  }

  /**
   * Instantiates a new j scrolling text.
   * 
   * @param text 
   * @param speed 
   */
  public JScrollingText(String text, int speed) {
    this(text, speed, 100);
  }

  /**
   * Instantiates a new j scrolling text.
   * 
   * @param text 
   * @param speed 
   * @param period 
   */
  public JScrollingText(String text, int speed, int period) {
    this(text, speed, period, 0);
  }

  /**
   * Instantiates a new j scrolling text.
   * 
   * @param text 
   * @param speed 
   * @param period 
   * @param offset 
   */
  public JScrollingText(String text, int speed, int period, int offset) {
    super(text);
    this.speed = speed;
    this.period = period;
    this.offset = offset;
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paintComponent(Graphics g) {
    if (isOpaque()) {
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    g.setColor(getForeground());
    FontMetrics fm = g.getFontMetrics();
    Insets insets = getInsets();
    int width = getWidth() - (insets.left + insets.right);
    int height = getHeight() - (insets.top + insets.bottom);
    int textWidth = fm.stringWidth(getText());
    if (width < textWidth) {
      width = textWidth + offset;
    }
    x %= width;
    int textX = insets.left + x;
    int textY = insets.top + (height - fm.getHeight()) / 2 + fm.getAscent();
    g.drawString(getText(), textX, textY);
    g.drawString(getText(), textX + (speed > 0 ? -width : width), textY);
  }

  Timer timer;

  /**
   * Start.
   * 
   */
  public void start() {
    timer = new Timer("Scrolling Text Timer");
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        x += speed;
        repaint();
      }
    };
    timer.scheduleAtFixedRate(task, 1000, period);
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#removeNotify()
   */
  @Override
  public void removeNotify() {
    // clean out the timer
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
    super.removeNotify();
  }
}
