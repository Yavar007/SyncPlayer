package org.yavar007.forms;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.yavar007.database.RoomDatabaseHelper;
import org.yavar007.interfaces.IMainViewControls;
import org.yavar007.misc.MessageHandling;
import org.yavar007.misc.RoomIdGenerator;
import org.yavar007.misc.ToastHelper;
import org.yavar007.models.ClientModel;
import org.yavar007.models.CommunicationModels.PlayerMessageModel;
import org.yavar007.models.CommunicationModels.RequestToJoinMessageModel;
import org.yavar007.models.LanguageModels.LanguageModel;
import org.yavar007.models.UsersInRoomModel;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.TrackDescription;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PlayerForm represents the Player form of the SyncPlayer application.
 * It handles Player Controls, users List Open/Close.
 */
public class PlayerForm implements IMainViewControls {
    private boolean isSeeking = false;
    private boolean isFullScreen = false;
    private boolean arePanelsHidden = false;
    private final boolean isServer;
    private JFrame frame;
    private String roomID;
    private final MessageHandling messageHandling;
    private final ClientModel clientModel;
    private String movieURL = "";
    private MediaPlayer mediaPlayer;
    //private String movieTime = "";
    private boolean isPlaying = false;
    private int movieTimeDelay = 500;
    private boolean isPlayerReady = false;
    private final LanguageModel languageModel;
    private UsersListPanel usersListPanel;
    private boolean isUsersListOpen = false;
    private final RoomDatabaseHelper roomDatabaseHelper;
    private final MqttClient mqttClient;
    private Point initialClick; // For dragging the window
    public PlayerForm(MqttClient client, ClientModel clientModel, boolean isServer, String username, LanguageModel languageModel) {
        this.isServer = isServer;
        this.clientModel = clientModel;
        this.languageModel = languageModel;
        this.mqttClient = client;
        if (isServer) {
            roomID = new RoomIdGenerator().generateRoomId();
        }
        roomDatabaseHelper=new RoomDatabaseHelper();
        messageHandling = new MessageHandling(this, client, clientModel);
        createAndSetSwingContent(username);

    }

    private void createAndSetSwingContent(String username) {

        //region UI
        frame = new JFrame(languageModel.getSecondForm().getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(true);

        //region TitleBar
        // Title bar panel
        JPanel titleBar = new JPanel(new BorderLayout());
        // Username label
        JLabel userNameLabel = new JLabel(languageModel.getSecondForm().getFrameTitle().formatted(username));
        userNameLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        // Panel to hold the title bar buttons
        JPanel titleButtonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        // Custom title bar
        JButton minimizeButton = new JButton("-");
        JButton maximizeButton = new JButton("□");
        JButton closeButton = new JButton("X");
        titleButtonHolder.setBackground(Color.GRAY);
        titleButtonHolder.setBorder(new EmptyBorder(0, 0, 0, 4));
        titleButtonHolder.add(minimizeButton);
        titleButtonHolder.add(maximizeButton);
        titleButtonHolder.add(closeButton);

        titleBar.setBackground(Color.GRAY);
        titleBar.add(userNameLabel, BorderLayout.WEST);
        titleBar.add(titleButtonHolder, BorderLayout.EAST);



        //endregion

        //region Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JButton loadButton =
                new JButton(
                        isServer?
                                languageModel
                                        .getSecondForm()
                                        .getBtnServerLoad():
                                languageModel
                                        .getSecondForm()
                                        .getBtnClientJoin());

        // Create a text field for the URL input
        JTextField urlField = new JTextField("", 30);

        // Create a panel for the URL input and load button
        JPanel urlPanel = new JPanel();
        JLabel urlRoomIdHint = new JLabel();

        urlRoomIdHint.setText(isServer?
                languageModel
                        .getSecondForm()
                        .getServerUrlFiledHint():
                languageModel
                        .getSecondForm()
                        .getClientRoomIDFieldHint());
        urlField.setToolTipText(isServer?
                languageModel
                        .getSecondForm()
                        .getServerUrlFiledToolTip():
                languageModel
                        .getSecondForm()
                        .getClientRoomIDFieldToolTip());
        JLabel roomIDLabel = new JLabel();
        roomIDLabel.setText(isServer?roomID:"");
        roomIDLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        if (languageModel.isRTL()){
            roomIDLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        }
        roomIDLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Get the text from the JLabel
                String textToCopy = roomIDLabel.getText();
                if (!textToCopy.isEmpty()) {
                    // Copy the text to the clipboard
                    StringSelection stringSelection = new StringSelection(textToCopy);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                    showToast(languageModel.getSecondForm().getOnRoomIDCopied().formatted(textToCopy));
                }

            }
        });
        urlPanel.add(roomIDLabel);
        urlPanel.add(urlRoomIdHint);
        urlPanel.add(urlField);
        urlPanel.add(loadButton);

        headerPanel.add(titleBar);
        headerPanel.add(urlPanel);
        //endregion

        //region Control Panel
        JPanel leftPanelDelay = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField delay = new JTextField(String.valueOf(movieTimeDelay), 10);
        delay.setEnabled(!isServer);
        delay.setVisible(!isServer);
        leftPanelDelay.add(delay);
        // Create a panel for the control buttons
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        JPanel buttonsPanel = new JPanel();
        // Create control buttons
        JButton playButton = new JButton(languageModel.getSecondForm().getBtnPlay());
        JButton pauseButton = new JButton(languageModel.getSecondForm().getBtnPause());
        JButton stopButton = new JButton(languageModel.getSecondForm().getBtnStop());
        JButton audioButton = new JButton(languageModel.getSecondForm().getBtnAudio());
        JButton subtitleButton = new JButton(languageModel.getSecondForm().getBtnSubtitle());
        JButton showUsersListButton = new JButton(languageModel.getSecondForm().getBtnShowUsers());
        playButton.setEnabled(isServer);
        pauseButton.setEnabled(isServer);
        stopButton.setEnabled(isServer);
        audioButton.setEnabled(isServer);
        subtitleButton.setEnabled(isServer);

        buttonsPanel.add(leftPanelDelay);
        buttonsPanel.add(playButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(audioButton);
        buttonsPanel.add(subtitleButton);
        buttonsPanel.add(showUsersListButton);

        //buttonsPanel.add(fullScreenButton);
        JLabel movieTimeText = new JLabel("00:00:00");

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(movieTimeText);
        // Create a seek bar
        JSlider seekBar = new JSlider();
        seekBar.setEnabled(isServer);
        seekBar.setVisible(isServer);
        seekBar.setMinimum(0);
        //seekBar.setMaximum(1000);
        seekBar.setValue(0);
        controlsPanel.add(seekBar);
        controlsPanel.add(leftPanel);
        controlsPanel.add(buttonsPanel);
        //endregion

        //region Font & RTL
        Font customFont = new Font("Segoe UI", Font.PLAIN, 14);
        if (languageModel.getLanguageCode().equals("per")) {
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/yekan.ttf")).deriveFont(12f);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }
            applyRTL(controlsPanel);
            applyRTL(urlPanel);
            applyRTL(userNameLabel);
            applyLTR(seekBar);
        }
        userNameLabel.setFont(customFont);
        urlRoomIdHint.setFont(customFont);
        loadButton.setFont(customFont);
        playButton.setFont(customFont);
        pauseButton.setFont(customFont);
        stopButton.setFont(customFont);
        audioButton.setFont(customFont);
        subtitleButton.setFont(customFont);
        showUsersListButton.setFont(customFont);
        //endregion

        //region Player
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        // Create an embedded media player component
        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayer = mediaPlayerComponent.mediaPlayer();
        //endregion


        // Add the components to the frame in the desired order
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mediaPlayerComponent, BorderLayout.CENTER);
        frame.add(controlsPanel, BorderLayout.SOUTH);
        // Set the frame size and make it visible
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        disableFocusForNonTextComponents(frame);
        //endregion
        //region Background Tasks
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            if (mediaPlayer.status().isPlaying()) {
                int sub = mediaPlayer.subpictures().track();
                int audio = mediaPlayer.audio().track();
                String msg = mediaPlayer.status().time() + ":" + sub + ":" + audio;
                PlayerMessageModel play = new PlayerMessageModel(clientModel.getId(),
                        "play", clientModel.getDeviceName(), clientModel.getDeviceOs(),
                        "server",username, msg, roomID);
                messageHandling.sendMessage(roomID, play);
                //System.out.println("Time Sent: " + msg);
            } else {
                String msg = "paused";
                PlayerMessageModel play = new PlayerMessageModel(clientModel.getId(),
                        "play", clientModel.getDeviceName(), clientModel.getDeviceOs(),
                        "server",username, msg, roomID);
                messageHandling.sendMessage(roomID, play);

            }

        };
        ScheduledExecutorService specChecker = Executors.newScheduledThreadPool(1);
        specChecker.scheduleAtFixedRate(() -> {
            try{
                List<UsersInRoomModel> users = roomDatabaseHelper.selectAllData();
                if (users.size()>1){
                    users.parallelStream() .filter(user -> !user.getID().equals(clientModel.getId()))
                            .forEach(user -> {
                                long elapsedTime = (System.currentTimeMillis() - user.getLastAlive()) / 1000;
                                try { if (elapsedTime > 10) {
                                    roomDatabaseHelper.deleteData(user);
                                    if (user.getClientRole().equals("server")){
                                        mediaPlayer.controls().stop();
                                        mediaPlayer.release();
                                        showToast(languageModel.getSecondForm().getOnRoomOwnerDisconnected());
                                    }else{
                                        showToast(languageModel.getSecondForm().getOnUserDisconnected().formatted(user.getNickName()));
                                    }
                                    System.out.println("User " + user.getNickName() + " Disconnected");
                                    SwingUtilities.invokeLater(() -> usersListPanel.updateUsers());
                                } else if (elapsedTime > 5) {
                                    user.setOnline(false);
                                    roomDatabaseHelper.updateData(user);
                                    System.out.println("User " + user.getNickName() + " is Offline");
                                    SwingUtilities.invokeLater(() -> usersListPanel.updateUsers());
                                } } catch (Exception e) {
                                    System.out.println("Error processing user: " + e.getMessage());
                                }
                            });
                }

            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }


        }, 0,1 , TimeUnit.SECONDS); // Check every 1 seconds
        //endregion
        //region Components Listeners

        // Add MouseListener and MouseMotionListener for dragging
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                titleBar.getComponentAt(initialClick); // To bring the frame to front
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                usersListPanel.closeUsersList();
                isUsersListOpen = false;
                // get location of Window
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                frame.setLocation(X, Y);
            }
        });

        Color minimizeButtonbackGroundColor=minimizeButton.getBackground();
        Color minimizeButtonforeGroundColor=minimizeButton.getForeground();
        minimizeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                minimizeButton.setBackground(Color.orange);
                minimizeButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimizeButton.setBackground(minimizeButtonbackGroundColor);
                minimizeButton.setForeground(minimizeButtonforeGroundColor);
            }
        });
        Color closeButtonbackGroundColor=closeButton.getBackground();
        Color closeButtonforeGroundColor=closeButton.getForeground();
        closeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(Color.RED);
                closeButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(closeButtonbackGroundColor);
                closeButton.setForeground(closeButtonforeGroundColor);
            }
        });
        minimizeButton.addActionListener(_ -> {
            frame.setState(JFrame.ICONIFIED);
            usersListPanel.closeUsersList();
            isUsersListOpen = false;
        });
        maximizeButton.addActionListener(_ -> {
            usersListPanel.closeUsersList();
            isUsersListOpen = false;
            if (isFullScreen) {
                isFullScreen = false;
                frame.setExtendedState(JFrame.NORMAL);
                frame.setSize(800, 600);

            } else {
                isFullScreen = true;
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            }
            usersListPanel=new UsersListPanel(isServer,frame,languageModel.getSecondForm());
        });
        closeButton.addActionListener(_ -> System.exit(0));
        delay.addActionListener(e -> {
            String ac = e.getActionCommand();
            if (ac.length()>2){
                int d=Integer.parseInt(ac);
                if (d>=500){
                    movieTimeDelay = d;
                }
                else{
                    showToast(languageModel.getSecondForm().getDelayLowValue());
                }
            }


        });
        playButton.addActionListener(_ -> {

            if (isPlayerReady) {
                if(!isPlaying){
                    mediaPlayerComponent.mediaPlayer().controls().play();
                    isPlaying = true;
                }
            }
        });
        pauseButton.addActionListener(_ -> {
            if (isPlayerReady) {
                if (isPlaying){
                    mediaPlayerComponent.mediaPlayer().controls().pause();
                    isPlaying = false;
                }

            }

        });
        stopButton.addActionListener(_ -> {

            if (isPlayerReady) {
                mediaPlayerComponent.mediaPlayer().controls().stop();
            }
        });
        audioButton.addActionListener(_ -> {
            if (isPlayerReady) {
                int currentTrack = mediaPlayer.audio().track();
                int[] tracks = mediaPlayer.audio().trackDescriptions().stream().mapToInt(TrackDescription::id).toArray();
                int nextTrack = currentTrack + 1;
                if (nextTrack > tracks.length - 1) {
                    nextTrack = 0;
                } else if (currentTrack == -1) {
                    nextTrack = 1;
                }

                mediaPlayer.audio().setTrack(tracks[nextTrack]);
                System.out.println("Switched to Audio Track ID: " + tracks[nextTrack]);
            }

        });
        subtitleButton.addActionListener(_ -> {
            if (isPlayerReady) {
                // Get the current subtitle track
                int currentTrack = mediaPlayer.subpictures().track();
                // Fetch all subtitle track IDs
                int[] tracks = mediaPlayer.subpictures().trackDescriptions()
                        .stream()
                        .mapToInt(TrackDescription::id)
                        .toArray();
                int nextTrack = currentTrack + 1;
                if (nextTrack > tracks.length - 1) {
                    nextTrack = 0; // Loop back to the first track
                } else if (currentTrack == -1) {
                    nextTrack = 1; // Start with the first track if none is currently selected
                }

                // Switch to the next subtitle track
                mediaPlayer.subpictures().setTrack(tracks[nextTrack]);
                System.out.println("Switched to Subtitle Track ID: " + tracks[nextTrack]);
            }
        });
        //region Media Player Events
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventListener() {
            @Override
            public void mediaChanged(MediaPlayer mediaPlayer, MediaRef mediaRef) {
            }
            @Override
            public void opening(MediaPlayer mediaPlayer) {
            }
            @Override
            public void buffering(MediaPlayer mediaPlayer, float v) {
            }
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }
            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }
            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }
            @Override
            public void forward(MediaPlayer mediaPlayer) {

            }
            @Override
            public void backward(MediaPlayer mediaPlayer) {

            }
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                long actualEndTime = mediaPlayer.status().time();

                SwingUtilities.invokeLater(() -> seekBar.setMaximum((int) (1000 * actualEndTime / mediaPlayer.media().info().duration())));
            }
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long l) {
                if (!isSeeking) {
                    long length = mediaPlayer.media().info().duration();
                    if (length > 0) {
                        int value = (int) (1000 * l / length);
                        //movieTime = String.valueOf(mediaPlayer.status().time());
                        movieTimeText.setText(convertLongToTime(mediaPlayer.status().time()));
                        SwingUtilities.invokeLater(() -> seekBar.setValue(value));
                    }
                }

            }
            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float v) {
            }
            @Override
            public void seekableChanged(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void pausableChanged(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void titleChanged(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String s) {
            }
            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long l) {
            }
            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void scrambledChanged(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType trackType, int i) {
            }
            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType trackType, int i) {
            }
            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType trackType, int i) {
            }
            @Override
            public void corked(MediaPlayer mediaPlayer, boolean b) {
            }
            @Override
            public void muted(MediaPlayer mediaPlayer, boolean b) {
            }
            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float v) {
            }
            @Override
            public void audioDeviceChanged(MediaPlayer mediaPlayer, String s) {
            }
            @Override
            public void chapterChanged(MediaPlayer mediaPlayer, int i) {
            }
            @Override
            public void error(MediaPlayer mediaPlayer) {
            }
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                seekBar.setEnabled(true);
                isPlayerReady = true;
                if (isServer){
                    int[] subTracks = mediaPlayer.subpictures().trackDescriptions()
                            .stream()
                            .mapToInt(TrackDescription::id)
                            .toArray();
                    int[] audioTracks = mediaPlayer.audio().trackDescriptions().stream().mapToInt(TrackDescription::id).toArray();
                    subtitleButton.setEnabled(subTracks.length > 0);
                    audioButton.setEnabled(audioTracks.length > 0);
                }
                long duration = mediaPlayer.media().info().duration();
                SwingUtilities.invokeLater(() -> seekBar.setMaximum((int) (1000 * duration / duration)));
            }
        });
        //endregion
        //region SeekBar Mouse Events
        seekBar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                isSeeking = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (seekBar.isEnabled()) {
                    isSeeking = false;
                    long length = mediaPlayer.media().info().duration();
                    long time = length * seekBar.getValue() / 1000;
                    mediaPlayer.controls().setTime(time);
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //endregion

        // Load new video URL
        loadButton.addActionListener(_ -> {
            if (mqttClient.isConnected()){
                if (!urlField.getText().isEmpty()) {
                    if (isServer) {

                        movieURL = urlField.getText();
                        if (isURLValid(movieURL)){
                            messageHandling.Run(isServer, roomID, movieURL,username);
                            mediaPlayer.media().play(movieURL);
                            scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
                            isPlaying = true;
                        }else{
                            showToast(languageModel.getSecondForm().getInvalidURL());
                        }


                    } else {
                        roomID = urlField.getText();
                        messageHandling.Run(isServer, roomID, "",username);
                        messageHandling.sendMessage(roomID,
                                new RequestToJoinMessageModel(clientModel.getId()
                                        , "req", clientModel.getDeviceName(), clientModel.getDeviceOs(), "client", roomID, username));
                    }
                } else {
                    if (isServer) {
                        showToast(languageModel.getSecondForm().getOnURLEmpty());
                    } else {
                        showToast(languageModel.getSecondForm().getOnRoomIDEmpty());
                    }
                }
            }else{
                showToast(languageModel.getMainMessageTexts().getOnDisconnected());
            }
        });

        mediaPlayerComponent.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usersListPanel.closeUsersList();
                isUsersListOpen = false;
                if (arePanelsHidden) {
                    controlsPanel.setVisible(true);
                    headerPanel.setVisible(true);
                    arePanelsHidden = false;
                } else {
                    controlsPanel.setVisible(false);
                    headerPanel.setVisible(false);
                    arePanelsHidden = true;
                }
            }
        });
        mediaPlayerComponent.videoSurfaceComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (isPlayerReady){
                        if (isPlaying){
                            mediaPlayer.controls().pause();
                            isPlaying = false;
                        }
                        else{
                            mediaPlayer.controls().play();
                            isPlaying = true;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        //endregion
        //region SidebarUI
        String role=isServer ? "server" : "client";
        roomDatabaseHelper.insertData(new UsersInRoomModel(clientModel.getId()
                , username
                , clientModel.getDeviceOs()
                , role
                , true, System.currentTimeMillis()));

        usersListPanel = new UsersListPanel(isServer,frame,languageModel.getSecondForm());
        showUsersListButton.addActionListener(_ -> {
            if (isUsersListOpen) {
                usersListPanel.closeUsersList();
                isUsersListOpen = false;
            } else {
                usersListPanel.openUsersList(languageModel.isRTL());
                isUsersListOpen = true;
            }

        });
        //endregion
    }

    private boolean isURLValid(String url){
        String regex = "^(https?://)?(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[\\w-]+|https?://\\S+?\\.(mp4|mkv)((\\?\\S*)?(#\\S*)?|(#\\S*)?(\\?\\S*)?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
    public static void applyRTL(Component component) {
        component.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyRTL(child);
            }
        }
    }
    public static void applyLTR(Component component) {
        component.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyLTR(child);
            }
        }
    }
    public static String convertLongToTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    @Override
    public void setMovieLink(String movieLink) {
        movieURL = movieLink;
        mediaPlayer.media().play(movieLink);
    }
    @Override
    public void setMovieTime(String movieTime) {
        System.out.println("Time Received : " + movieTime);
        if (movieTime.equals("paused")) {
            if (mediaPlayer.status().isPlaying()) {
                mediaPlayer.controls().pause();
            }
        } else {
            if (!mediaPlayer.status().isPlaying()) {
                mediaPlayer.controls().play();
            }
            if (movieTime.contains(":")) {
                long movetime = Long.parseLong(movieTime.split(":")[0]);
                long currenttime = mediaPlayer.status().time();
                long delay = movetime - currenttime;
                int sub = Integer.parseInt(movieTime.split(":")[1]);
                int track = Integer.parseInt(movieTime.split(":")[2]);
                if (sub != mediaPlayer.subpictures().track()) {
                    mediaPlayer.subpictures().setTrack(sub);
                }
                if (track != mediaPlayer.audio().track()) {
                    mediaPlayer.audio().setTrack(track);
                }
                if (delay > -movieTimeDelay && delay < movieTimeDelay) {
                    System.out.println("Delay : " + delay);
                    System.out.println("Current Playing Time : " + currenttime);
                } else {
                    mediaPlayer.controls().setTime(movetime);
                    System.out.println("Time set to :" + movetime);
                    if (!mediaPlayer.status().isPlaying()) {
                        mediaPlayer.controls().play();
                    }
                }
            }
        }
    }
    @Override
    public void addNewSpectator(UsersInRoomModel spectator) {
        String username = spectator.getNickName();
        showToast(languageModel.getSecondForm().getOnNewUserJoined().formatted(username));
        if (isUsersListOpen){
            usersListPanel.closeUsersList();
        }
        roomDatabaseHelper.insertData(spectator);
        usersListPanel.updateUsers();
    }
    public void editSpectator(UsersInRoomModel spectator){
        UsersInRoomModel usersInRoom=roomDatabaseHelper.selectData(spectator.getID());
        if (usersInRoom == null) {
            addNewSpectator(spectator);
            return;
        }
        System.out.println(spectator.getNickName() + " Sends Alive Message, Update Time= "+spectator.getLastAlive());
        roomDatabaseHelper.updateData(spectator);
        usersListPanel.updateUsers();
    }
    @Override
    public void showToast(String message) {
        ToastHelper toastHelper = new ToastHelper(message, frame);
        toastHelper.showMessage();
    }
    public static void disableFocusForNonTextComponents(Container container) {
        for (Component component : container.getComponents()) {
            // Skip text input components (JTextField, JTextArea, etc.)
            if (!(component instanceof JTextField)) {
                component.setFocusable(false); // Disable focus for non-text components

                // Add a MouseListener to re-enable focus on click
                component.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        component.setFocusable(true); // Re-enable focus
                        component.requestFocusInWindow(); // Request focus
                    }
                });
            }

            // If the component is a container, recursively process its children
            if (component instanceof Container) {
                disableFocusForNonTextComponents((Container) component);
            }
        }
    }
}