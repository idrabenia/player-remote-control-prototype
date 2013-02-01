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
package org.jajuk.base;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jajuk.ConstTest;
import org.jajuk.JUnitHelpers;
import org.jajuk.JajukTestCase;
import org.jajuk.base.TestAlbumManager.MockPlayer;
import org.jajuk.services.core.ExitService;
import org.jajuk.services.players.IPlayerImpl;
import org.jajuk.services.players.QueueModel;
import org.jajuk.services.players.StackItem;
import org.jajuk.services.startup.StartupCollectionService;
import org.jajuk.util.Const;
import org.jajuk.util.error.JajukException;
import org.jajuk.util.log.Log;
import org.xml.sax.Attributes;

/**
 * .
 */
public class TestDevice extends JajukTestCase {
  /*
   * (non-Javadoc)
   *
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getTitle()}.
   */
  public void testGetDesc() {
    Device device = new Device("1", "testname");
    assertNotNull(device.getTitle());
    assertFalse(device.getTitle().length() == 0);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getXMLTag()}.
   */
  public void testGetLabel() {
    Device device = JUnitHelpers.getDevice();
    assertEquals(Const.XML_DEVICE, device.getXMLTag());
  }

  /**
   * Test method for.
   *
   * {@link org.jajuk.base.Device#populateProperties(org.xml.sax.Attributes)}.
   */
  public void testPopulateProperties() {
    StartupCollectionService.registerItemManagers();
    Device device = new Device("1", "name");
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att1", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att2", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att3", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation(Const.XML_DEVICE_AUTO_REFRESH, true, false, true, false, false,
            Double.class, null));
    device.populateProperties(new MockAttributes());
    device = DeviceManager.getInstance().registerDevice("name", Device.Type.FILES_CD,
        ConstTest.DEVICES_BASE_PATH + "/cd");
    device.populateProperties(new MockAttributes());
    device = DeviceManager.getInstance().registerDevice("name", Device.Type.NETWORK_DRIVE,
        ConstTest.DEVICES_BASE_PATH + "/net");
    device.populateProperties(new MockAttributes());
    device = DeviceManager.getInstance().registerDevice("name", Device.Type.EXTDD,
        ConstTest.DEVICES_BASE_PATH + "/extdd");
    device.populateProperties(new MockAttributes());
    device = DeviceManager.getInstance().registerDevice("name", Device.Type.PLAYER,
        ConstTest.DEVICES_BASE_PATH + "/player");
    device.populateProperties(new MockAttributes());
  }

  /**
   * Test method for.
   *
   * {@link org.jajuk.base.Device#getHumanValue(java.lang.String)}.
   */
  public void testGetHumanValue() {
    // we need the managers registered here
    StartupCollectionService.registerItemManagers();
    Device device = new Device("1", "testname");
    assertEquals("", device.getHumanValue("notexists"));
    // TODO: capture correct use of this method as well
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att1", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att2", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation("att3", true, false, true, false, false, String.class, null));
    DeviceManager.getInstance().registerProperty(
        new PropertyMetaInformation(Const.XML_DEVICE_AUTO_REFRESH, true, false, true, false, false,
            Double.class, null));
    device.populateProperties(new MockAttributes());
    assertNotNull(device.getHumanValue("att1"));
    device.setProperty(Const.XML_TYPE, 2l);
    assertTrue(StringUtils.isNotBlank(device.getHumanValue(Const.XML_TYPE)));
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getIconRepresentation()}.
   *
   * @throws Exception the exception
   */
  public void testGetIconRepresentation() throws Exception {
    // we need the managers registered here
    StartupCollectionService.registerItemManagers();
    Device device = JUnitHelpers.getDevice();
    assertNotNull(device.getIconRepresentation());
    device.setProperty(Const.XML_TYPE, (long) Device.Type.DIRECTORY.ordinal());
    assertNotNull(device.getIconRepresentation());
    device.setProperty(Const.XML_TYPE, (long) Device.Type.FILES_CD.ordinal());
    assertNotNull(device.getIconRepresentation());
    device.setProperty(Const.XML_TYPE, (long) Device.Type.NETWORK_DRIVE.ordinal());
    assertNotNull(device.getIconRepresentation());
    device.setProperty(Const.XML_TYPE, (long) Device.Type.EXTDD.ordinal());
    assertNotNull(device.getIconRepresentation());
    device.setProperty(Const.XML_TYPE, (long) Device.Type.PLAYER.ordinal());
    assertNotNull(device.getIconRepresentation());
    // test with mounted device
    device.mount(true);
    device.setProperty(Const.XML_TYPE, (long) Device.Type.PLAYER.ordinal());
    assertNotNull(device.getIconRepresentation());
  }

  /**
   * Test method for.
   *
   * {@link org.jajuk.base.Device#addDirectory(org.jajuk.base.Directory)}.
   */
  public void testAddDirectory() {
    Device device = JUnitHelpers.getDevice();
    Directory dir1 = DirectoryManager.getInstance().registerDirectory("dir1",
        JUnitHelpers.getDirectory(), device);
    Directory dir2 = DirectoryManager.getInstance().registerDirectory("dir2",
        JUnitHelpers.getDirectory(), device);
    assertEquals(0, device.getDirectories().size());
    device.addDirectory(dir1);
    assertEquals(1, device.getDirectories().size());
    device.addDirectory(dir2);
    assertEquals(2, device.getDirectories().size());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#cleanRemovedFiles()}.
   *
   * @throws Exception the exception
   */
  public void testCleanRemovedFiles() throws Exception {
    Device device = JUnitHelpers.getDevice();
    Playlist playlist = JUnitHelpers.getPlaylist();
    // ensure we are not exiting, this would invalidate the test
    assertFalse(ExitService.isExiting());
    // Delete a file
    playlist.getFIO().delete();
    // now we have removals
    List<Directory> dirs = new ArrayList<Directory>();
    dirs.add(playlist.getDirectory());
    assertTrue(device.cleanRemovedFiles(dirs));
  }

  /**
   * Test method for.
   *
   * {@link org.jajuk.base.Device#compareTo(org.jajuk.base.Device)}.
   */
  public void testCompareTo() {
    Device device = new Device("1", "name");
    Device equal = new Device("1", "name");
    Device notequal = new Device("1", "name1"); // compares only on name
    JUnitHelpers.CompareToTest(device, equal, notequal);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getDateLastRefresh()}.
   *
   * @throws Exception the exception
   */
  public void testGetDateLastRefresh() throws Exception {
    Device device = JUnitHelpers.getDevice();
    assertEquals(0, device.getDateLastRefresh());
    device.mount(false);
    device.refreshCommand(false, false, null);
    // now it should be set
    assertNotNull(device.getDateLastRefresh());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getDeviceTypeS()}.
   */
  public void testGetDeviceTypeS() {
    Device device = JUnitHelpers.getDevice();
    assertNotNull(device.getDeviceTypeS());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getDirectories()}.
   */
  public void testGetDirectories() {
    Device device = JUnitHelpers.getDevice();
    assertEquals(0, device.getDirectories().size());
    Directory dir = JUnitHelpers.getDirectory();
    device.addDirectory(dir);
    assertEquals(1, device.getDirectories().size());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getFilesRecursively()}.
   */
  public void testGetFilesRecursively() {
    Device device = JUnitHelpers.getDevice();
    device.setUrl(ConstTest.DEVICES_BASE_PATH + "/" + System.currentTimeMillis());
    // no files without a directory
    List<File> files = device.getFilesRecursively();
    assertEquals(0, files.size()); // no file available
    Directory dir = DirectoryManager.getInstance().registerDirectory(device);
    // still no files without files being registered
    files = device.getFilesRecursively();
    assertEquals(0, files.size()); // no file available
    getFile(100, dir);
    // now it should find some
    files = device.getFilesRecursively();
    assertEquals(1, files.size());
  }

  /**
   * Gets the file.
   *
   * @param i 
   * @param dir 
   * @return the file
   */
  @SuppressWarnings("unchecked")
  private File getFile(int i, Directory dir) {
    Genre genre = JUnitHelpers.getGenre("name");
    Album album = JUnitHelpers.getAlbum("myalbum", 0);
    album.setProperty(Const.XML_ALBUM_DISCOVERED_COVER, Const.COVER_NONE); // don't read covers for
    // this test
    Artist artist = JUnitHelpers.getArtist("name");
    Year year = new Year(Integer.valueOf(i).toString(), "2000");
    IPlayerImpl imp = new MockPlayer();
    Class<IPlayerImpl> cl = (Class<IPlayerImpl>) imp.getClass();
    Type type = new Type(Integer.valueOf(i).toString(), "name", "mp3", cl, null);
    Track track = new Track(Integer.valueOf(i).toString(), "name", album, genre, artist, 120, year,
        1, type, 1);
    return FileManager.getInstance().registerFile(
        "test" + Long.valueOf(System.currentTimeMillis()).toString() + ".tst", dir, track, 120, 70);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getFIO()}.
   */
  public void testGetFio() {
    Device device = JUnitHelpers.getDevice();
    device.setUrl(ConstTest.TEMP_PATH);
    assertNotNull(device.getFIO());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getRootDirectory()}.
   *
   * @throws Exception the exception
   */
  public void testGetRootDirectory() throws Exception {
    // create a unique id here...
    Device device = DeviceManager.getInstance().registerDevice("getRootDirectory",
        Device.Type.FILES_CD, ConstTest.DEVICES_BASE_PATH + "/device");
    assertNull(device.getRootDirectory());
    java.io.File file = java.io.File.createTempFile("test", "tst", new java.io.File(
        ConstTest.DEVICES_BASE_PATH));
    device.setUrl(file.getAbsolutePath());
    DirectoryManager.getInstance().registerDirectory(device);
    assertNotNull(device.getRootDirectory());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getType()}.
   */
  public void testGetType() {
    Device device = JUnitHelpers.getDevice();
    assertEquals(Device.Type.DIRECTORY, device.getType());
    device.setProperty(Const.XML_TYPE, 2l);
    assertEquals(Device.Type.NETWORK_DRIVE, device.getType());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#getUrl()}.
   */
  public void testGetUrl() {
    Device device = new Device("1", "name");
    assertNull(device.getUrl());
    device.setUrl(ConstTest.DEVICES_BASE_PATH + "/dev");
    assertNotNull(device.getUrl());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#isMounted()}.
   *
   * @throws Exception the exception
   */
  public void testIsMounted() throws Exception {
    Device device = JUnitHelpers.getDevice();
    device.unmount();
    assertFalse(device.isMounted());
    new java.io.File(device.getUrl()).mkdirs();
    device.mount(true);
    assertTrue(device.isMounted());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#isReady()}.
   *
   * @throws Exception the exception
   */
  public void testIsReady() throws Exception {
    Device device = new Device("1", "name");
    device.setUrl(ConstTest.DEVICES_BASE_PATH + "/dev");
    assertFalse(device.isReady());
    new java.io.File(device.getUrl()).mkdirs();
    // create a file (we need at least a single file in collection)
    new java.io.File(device.getUrl() + "/audio.mp3").createNewFile();
    device.mount(true);
    assertTrue(device.isReady());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#isRefreshing()}.
   */
  public void testIsRefreshing() {
    Device device = JUnitHelpers.getDevice();
    assertFalse(device.isRefreshing());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#isSynchronizing()}.
   */
  public void testIsSynchronizing() {
    Device device = JUnitHelpers.getDevice();
    assertFalse(device.isSynchronizing());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#prepareRefresh(boolean)}.
   *
   * @throws Exception the exception
   */
  public void testPrepareRefresh() throws Exception {
    Device device = JUnitHelpers.getDevice();
    device.prepareRefresh(false);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#mount(boolean)}.
   *
   * @throws Exception the exception
   */
  public void testMount() throws Exception {
    Device device = JUnitHelpers.getDevice();
    device.mount(true);
    // try a second time, should fail
    try {
      device.mount(true);
      fail();
    } catch (Exception e) {
      Log.error(e);
    }
    // try a device that has an invalid URL
    device = new Device("1", "name");
    device.setUrl("notexisting/not/adsf\\dtest");
    try {
      device.mount(true);
    } catch (JajukException e) {
      assertEquals(11, e.getCode());
    }
  }

  /**
   * Test method for {@link org.jajuk.base.Device#refresh(boolean)}.
   *
   * @throws Exception the exception
   */
  public void testRefreshBoolean() throws Exception {
    Device device = JUnitHelpers.getDevice();
    try {
      device.refresh(false, false, false, null);
    } catch (RuntimeException e) {
      // there can be a hidden HeadlessException here
      assertTrue(e.getCause().getMessage(), e.getCause() instanceof InvocationTargetException);
    }
    device.refresh(true, false, false, null);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#refresh(boolean, boolean)}.
   *
   * @throws Exception the exception
   */
  public void testRefreshBooleanBoolean() throws Exception {
    Device device = JUnitHelpers.getDevice();
    try {
      device.refresh(false, false, false, null);
    } catch (RuntimeException e) {
      // there can be a hidden HeadlessException here
      assertTrue(e.getCause().getMessage(), e.getCause() instanceof InvocationTargetException);
    }
    device.refresh(true, false, false, null);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#refreshCommand(boolean)}.
   */
  public void testRefreshCommand() {
    Device device = JUnitHelpers.getDevice();
    try {
      device.mount(false);
    } catch (Exception e) {
      Log.error(e);
      fail();
    }
    device.refreshCommand(false, false, null);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#refreshCommand(boolean)}.
   *
   * @throws Exception the exception
   */
  public void testRefreshCommandNoMoreAvailable() throws Exception {
    // We check that a device mounted but no more available cannot be refreshed
    Device device = new Device("1", "name");
    // Prepare a sample directory with at least a single file
    java.io.File fileOKDir = new java.io.File(ConstTest.DEVICES_BASE_PATH + "/foo643");
    fileOKDir.mkdirs();
    java.io.File sampleFile = new java.io.File(fileOKDir.getAbsoluteFile() + "/foo.mp3");
    sampleFile.createNewFile();
    device.setUrl(fileOKDir.getAbsolutePath());
    device.mount(false);
    device.refreshCommand(false, false, null);
    // fine, now rename the directory
    sampleFile.delete();
    fileOKDir.delete();
    // An error should happen here
    device.refreshCommand(false, false, null);
  }

  // test for a regression that was added
  /**
   * Test refresh command dont readd top directory.
   * 
   * @throws Exception the exception
   */
  public void testRefreshCommandDontReaddTopDirectory() throws Exception {
    Device device = JUnitHelpers.getDevice();
    device.mount(true);
    device.refreshCommand(false, false, null);
    // we should not have more than one top-directory!
    assertEquals(1, device.getDirectories().size());
    // even if we refresh some more, we should only have one, not multiple
    device.refreshCommand(false, false, null);
    // we should not have more than one top-directory!
    assertEquals(1, device.getDirectories().size());
    device.refreshCommand(false, false, null);
    // we should not have more than one top-directory!
    assertEquals(1, device.getDirectories().size());
    device.refreshCommand(false, false, null);
    // we should not have more than one top-directory!
    assertEquals(1, device.getDirectories().size());
    device.refreshCommand(false, false, null);
    // we should not have more than one top-directory!
    assertEquals(1, device.getDirectories().size());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#setUrl(java.lang.String)}.
   *
   * @throws Exception the exception
   */
  public void testSetUrl() throws Exception {
    Device device = JUnitHelpers.getDevice();
    // add some directory, then the remove should kick in!
    Directory dir = JUnitHelpers.getDirectory();
    device.addDirectory(dir);
    File file = getFile(8, dir);
    PlaylistManager.getInstance().registerPlaylistFile(file.getFIO(), dir);
    // now also the playlist should be reset
    device.setUrl(ConstTest.TEMP_PATH);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#synchronize(boolean)}.
   */
  public void testSynchronize() {
    Device device = JUnitHelpers.getDevice();
    device.synchronize(true);
    // nothing much happens here as there is no synchro-device set
    device.synchronize(false);
  }

  /**
   * Test synchronize conf set.
   * 
   */
  public void testSynchronizeConfSet() {
    Device device = JUnitHelpers.getDevice();
    Device dSrc = JUnitHelpers.getDevice("src", Device.Type.DIRECTORY, ConstTest.DEVICES_BASE_PATH
        + "/src_device");
    assertNotNull(dSrc);
    assertNotNull(dSrc.getID());
    assertNotNull(DeviceManager.getInstance().getDeviceByID(dSrc.getID()));
    // set the synchro-device
    device.setProperty(Const.XML_DEVICE_SYNCHRO_SOURCE, dSrc.getID());
    assertNotNull(DeviceManager.getInstance().getDeviceByID(dSrc.getID()));
    assertNotNull(DeviceManager.getInstance().getDeviceByID(device.getID()));
    device.synchronize(false);
    assertNotNull(DeviceManager.getInstance().getDeviceByID(dSrc.getID()));
    assertNotNull(DeviceManager.getInstance().getDeviceByID(device.getID()));
    device.synchronize(true);
    assertNotNull(DeviceManager.getInstance().getDeviceByID(dSrc.getID()));
    assertNotNull(DeviceManager.getInstance().getDeviceByID(device.getID()));
  }

  /**
   * Test method for {@link org.jajuk.base.Device#synchronizeCommand()}.
   */
  public void testSynchronizeCommand() {
    Device device = JUnitHelpers.getDevice();
    device.synchronizeCommand();
    // TODO do some real testing here
  }

  /**
   * Test synchronize command sync device.
   * 
   */
  public void testSynchronizeCommandSyncDevice() {
    Device device = JUnitHelpers.getDevice();
    try {
      device.mount(false);
    } catch (Exception e) {
      Log.error(e);
      fail();
    }
    // set the synchro-device
    Device sync = DeviceManager.getInstance().registerDevice("name2", Device.Type.DIRECTORY,
        ConstTest.DEVICES_BASE_PATH + "/device2");
    device.setProperty(Const.XML_DEVICE_SYNCHRO_SOURCE, sync.getID());
    device.synchronizeCommand();
  }

  /**
   * Test synchronize command sync device bidi.
   * 
   */
  public void testSynchronizeCommandSyncDeviceBidi() {
    Device device = JUnitHelpers.getDevice();
    try {
      if (!device.mount(false)) {
        fail();
      }
    } catch (Exception e) {
      Log.error(e);
      fail();
    }
    // set the synchro-device
    Device sync = DeviceManager.getInstance().registerDevice("name2", Device.Type.DIRECTORY,
        ConstTest.DEVICES_BASE_PATH + "/device2");
    try {
      new java.io.File(sync.getUrl()).mkdirs();
      new java.io.File(sync.getUrl() + "/audio1.mp3").createNewFile();
      if (!sync.mount(false)) {
        fail();
      }
    } catch (Exception e) {
      Log.error(e);
      fail();
    }
    device.setProperty(Const.XML_DEVICE_SYNCHRO_SOURCE, sync.getID());
    device.setProperty(Const.XML_DEVICE_SYNCHRO_MODE, Const.DEVICE_SYNCHRO_MODE_BI);
    sync.setProperty(Const.XML_DEVICE_SYNCHRO_SOURCE, device.getID());
    sync.setProperty(Const.XML_DEVICE_SYNCHRO_MODE, Const.DEVICE_SYNCHRO_MODE_BI);
    device.synchronizeCommand();
    new java.io.File(sync.getUrl()).delete();
  }

  /**
   * Test method for {@link org.jajuk.base.Device#test()}.
   */
  public void testTest() {
    Device device = DeviceManager.getInstance().registerDevice("name", Device.Type.DIRECTORY,
        ConstTest.DEVICES_BASE_PATH + "/device");
    assertFalse(device.test());
  }

  /**
   * Test test mounted.
   * 
   */
  public void testTestMounted() {
    Device device = JUnitHelpers.getDevice();
    assertTrue(device.test());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#toString()}.
   */
  public void testToString() {
    Device device = JUnitHelpers.getDevice();
    JUnitHelpers.ToStringTest(device);
    device = JUnitHelpers.getDevice();
    JUnitHelpers.ToStringTest(device);
    device = new Device(null, null);
    JUnitHelpers.ToStringTest(device);
  }

  /**
   * Test method for {@link org.jajuk.base.Device#unmount()}.
   *
   * @throws Exception the exception
   */
  public void testUnmount() throws Exception {
    Device device = JUnitHelpers.getDevice();
    assertFalse(device.isMounted());
    device.unmount();
    device.mount(true);
    assertTrue(device.isMounted());
    device.unmount();
    assertFalse(device.isMounted());
  }

  /**
   * Test method for {@link org.jajuk.base.Device#unmount(boolean, boolean)}.
   *
   * @throws Exception the exception
   */
  public void testUnmountBooleanBoolean() throws Exception {
    Device device = JUnitHelpers.getDevice();
    assertFalse(device.isMounted());
    device.unmount(false, false);
    device.mount(true);
    assertTrue(device.isMounted());
    device.unmount(false, false);
    assertFalse(device.isMounted());
  }

  /**
   * Test unmount boolean boolean queue.
   * 
   *
   * @throws Exception the exception
   */
  public void testUnmountBooleanBooleanQueue() throws Exception {
    Device device = JUnitHelpers.getDevice();
    device.mount(true);
    assertTrue(device.isMounted());
    Directory dir = JUnitHelpers.getDirectory();
    device.addDirectory(dir);
    File file = getFile(9, dir);
    QueueModel.insert(new StackItem(file), 0);
    QueueModel.goTo(0);
    device.unmount(false, false);
    assertTrue(device.isMounted()); // still mounted because there is a file
    // used on this device....
  }

  /**
   * .
   */
  private class MockAttributes implements Attributes {
    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(java.lang.String, java.lang.String)
     */
    @Override
    public String getValue(String uri, String localName) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(java.lang.String)
     */
    @Override
    public String getValue(String qName) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(int)
     */
    @Override
    public String getValue(int index) {
      switch (index) {
      case 0:
        return "value1";
      case 1:
        return "value2";
      case 2:
        return "value3";
      case 3:
        return "true";
      default:
        fail("invalid index: " + index);
        break;
      }
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getURI(int)
     */
    @Override
    public String getURI(int index) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(java.lang.String, java.lang.String)
     */
    @Override
    public String getType(String uri, String localName) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(java.lang.String)
     */
    @Override
    public String getType(String qName) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(int)
     */
    @Override
    public String getType(int index) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getQName(int)
     */
    @Override
    public String getQName(int index) {
      switch (index) {
      case 0:
        return "att1";
      case 1:
        return "att2";
      case 2:
        return "att3";
      case 3:
        return Const.XML_DEVICE_AUTO_REFRESH;
      default:
        fail("invalid index: " + index);
      }
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getLocalName(int)
     */
    @Override
    public String getLocalName(int index) {
      return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getLength()
     */
    @Override
    public int getLength() {
      return 4;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getIndex(java.lang.String, java.lang.String)
     */
    @Override
    public int getIndex(String uri, String localName) {
      return 0;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getIndex(java.lang.String)
     */
    @Override
    public int getIndex(String qName) {
      return 0;
    }
  }
}
