package org.yavar007.forms;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialDarkerIJTheme;
import com.jthemedetecor.OsThemeDetector;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.yavar007.misc.SettingsHelper;
import org.yavar007.misc.ToastHelper;
import org.yavar007.models.ClientModel;
import org.yavar007.models.LanguageModels.LanguageModel;
import org.yavar007.models.SettingsModel;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * InitForm represents the initial form of the SyncPlayer application.
 * It handles user preferences like language, theme, nickname, and role selection.
 */
public class InitForm {
    // Constants
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 450;
    private static final int LOGO_SIZE = 400;
    private static final int TEXT_FIELD_WIDTH = 300;
    private static final int TEXT_FIELD_HEIGHT = 40;
    private static final float LOGO_OPACITY = 0.2f;
    
    // UI Components
    private static JLabel usernameLabel;
    private static JButton themeButton;
    private static JComboBox<String> langButton;
    private static JLabel nickNameHint;
    private static JTextField userName;
    private static JButton clientButton;
    private static JButton serverButton;
    private static JPanel controlsPanel;
    private final JFrame frame;
    private final SettingsHelper settingsHelper;
    private final MqttClient client;
    private final ClientModel clientModel;
    //private boolean isDarkTheme;

    public InitForm(MqttClient client, ClientModel clientModel) {
        this.client = client;
        this.clientModel = clientModel;
        this.settingsHelper = new SettingsHelper();
        //this.isDarkTheme = settingsHelper.LoadSettings().getIsDarkTheme();
        this.frame = createMainFrame();
        initializeUI();
        setupEventListeners();
        showConnectionStatus();
    }

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(true);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        return frame;
    }

    private void initializeUI() {
        JLabel logoLabel = createLogoLabel();
        JPanel titleBar = createTitleBar();
        JLayeredPane contentPanel = createContentPanel(logoLabel);
        
        frame.add(titleBar, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        // Load initial UI text from settings
        LoadComponents();
        
        frame.setVisible(true);
    }

    private JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel();
        try {
            BufferedImage img = ImageIO.read(new File("./images/applogo.png"));
            Image scaledImg = img.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImg);
            logoLabel = new JLabel(icon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, LOGO_OPACITY));
                    super.paintComponent(g2d);
                    g2d.dispose();
                }
            };
            logoLabel.setBounds(0, 0, LOGO_SIZE, LOGO_SIZE);
        } catch (IOException e) {
            System.out.println("Error loading logo: " + e.getMessage());
        }
        return logoLabel;
    }

    private JPanel createTitleBar() {
        JButton minimizeButton = new JButton("─");
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
        JButton closeButton = new JButton("X");
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

        usernameLabel = new JLabel();
        usernameLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel titleButtonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
        titleButtonHolder.add(minimizeButton);
        titleButtonHolder.add(closeButton);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.add(usernameLabel, BorderLayout.WEST);
        titleBar.add(titleButtonHolder, BorderLayout.EAST);

        minimizeButton.addActionListener(_ -> frame.setState(JFrame.ICONIFIED));
        closeButton.addActionListener(_ -> System.exit(0));

        return titleBar;
    }

    private JLayeredPane createContentPanel(JLabel logoLabel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
        layeredPane.add(logoLabel, 0);

        controlsPanel = createControlsPanel();
        layeredPane.add(controlsPanel, 1);

        return layeredPane;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBounds(0, 0, LOGO_SIZE, LOGO_SIZE);

        panel.add(createThemeAndLanguagePanel());
        panel.add(createUserInputPanel());
        panel.add(createRoleButtonsPanel());

        return panel;
    }

    private JPanel createThemeAndLanguagePanel() {
        JPanel themeButtonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel langButtonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel themeLangHolder = new JPanel(new BorderLayout());
        themeLangHolder.setPreferredSize(new Dimension(frame.getWidth(), 50));

        OsThemeDetector detector = OsThemeDetector.getDetector();
        themeButton = new JButton(detector.isDark() ? "Dark" : "Light");
        themeButton.setFocusable(false);

        List<LanguageModel> languageModels = settingsHelper.LoadSettings().getLanguages();
        List<String> languages = new ArrayList<>();
        for (LanguageModel lang : languageModels) {
            languages.add(lang.getFirstForm().getLangButtonText());
        }
        langButton = new JComboBox<>(languages.toArray(new String[0]));
        langButton.setFocusable(false);

        themeButtonHolder.add(themeButton);
        langButtonHolder.add(langButton);
        themeLangHolder.add(themeButtonHolder, BorderLayout.WEST);
        themeLangHolder.add(langButtonHolder, BorderLayout.EAST);

        return themeLangHolder;
    }

    private JPanel createUserInputPanel() {
        nickNameHint = new JLabel();
        userName = new JTextField(15);
        userName.setHorizontalAlignment(JTextField.CENTER);
        userName.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        userName.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 100));
        infoPanel.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH, 100));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nickNameHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(nickNameHint);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(userName);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centeringPanel.add(infoPanel, gbc);

        return centeringPanel;
    }

    private JPanel createRoleButtonsPanel() {
        clientButton = new JButton();
        clientButton.setFocusable(false);
        serverButton = new JButton();
        serverButton.setFocusable(false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(clientButton);
        buttonsPanel.add(serverButton);

        return buttonsPanel;
    }

    private void setupEventListeners() {
        setupThemeButtonListener();
        setupLanguageButtonListener();
        setupRoleButtonListeners();
    }

    private void setupThemeButtonListener() {
        themeButton.addActionListener(_ -> {
            SettingsModel settings = settingsHelper.LoadSettings();
            LookAndFeel dark = new FlatMTMaterialDarkerIJTheme();
            LookAndFeel current = UIManager.getLookAndFeel();
            
            if (current.getID().equals(dark.getID())) {
                FlatLightLaf.setup();
                themeButton.setSelected(false);
                updateThemeButtonText(settings, false);
                settings.setIsDarkTheme(false);
                //isDarkTheme = false;
            } else {
                FlatMTMaterialDarkerIJTheme.setup();
                updateThemeButtonText(settings, true);
                settings.setIsDarkTheme(true);
                //isDarkTheme = true;
            }
            
            settingsHelper.SaveSettings(settings);
            SwingUtilities.updateComponentTreeUI(frame);
        });
    }

    private void updateThemeButtonText(SettingsModel settings, boolean isDark) {
        for (LanguageModel lang : settings.getLanguages()) {
            if (settings.getSelectedLanguageCode().equals(lang.getLanguageCode())) {
                themeButton.setText(isDark ? 
                    lang.getFirstForm().getThemeButtonTextDark() : 
                    lang.getFirstForm().getThemeButtonTextLight());
            }
        }
    }

    private void setupLanguageButtonListener() {
        langButton.addItemListener(e -> {
            SettingsModel settings = settingsHelper.LoadSettings();
            String selectedLanguage = e.getItem().toString();
            
            if (selectedLanguage.equals("English")) {
                applyLTR(controlsPanel);
                settings.setSelectedLanguageCode("en");
                setEnglishFonts();
            } else if (selectedLanguage.equals("پارسی")) {
                applyRTL(controlsPanel);
                settings.setSelectedLanguageCode("per");
                setPersianFonts();
            }
            
            settingsHelper.SaveSettings(settings);
            LoadComponents();
            SwingUtilities.updateComponentTreeUI(frame);
        });
    }

    private void setEnglishFonts() {
        Font customFont = new Font("Segoe UI", Font.PLAIN, 12);
        applyFontToComponents(customFont);
    }

    private void setPersianFonts() {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/yekan.ttf")).deriveFont(12f);
            applyFontToComponents(customFont);
        } catch (Exception ex) {
            setEnglishFonts();
        }
    }

    private void applyFontToComponents(Font font) {
        langButton.setFont(font);
        themeButton.setFont(font);
        clientButton.setFont(font);
        serverButton.setFont(font);
    }

    private void setupRoleButtonListeners() {
        clientButton.addActionListener(_ -> handleRoleSelection(false));
        serverButton.addActionListener(_ -> handleRoleSelection(true));
    }

    private void handleRoleSelection(boolean isServer) {
        String username = userName.getText();
        if (!username.isEmpty()) {
            showSecondForm(client, clientModel, isServer, username);
            frame.dispose();
        } else {
            showEmptyNicknameMessage();
        }
    }

    private void showEmptyNicknameMessage() {
        SettingsModel settingsModel = settingsHelper.LoadSettings();
        for (LanguageModel lang : settingsModel.getLanguages()) {
            if (settingsModel.getSelectedLanguageCode().equals(lang.getLanguageCode())) {
                showMessage(frame, lang.getFirstForm().getOnNickNameEmpty());
            }
        }
    }

    private void showConnectionStatus() {
        SettingsModel settingsModel = settingsHelper.LoadSettings();
        for (LanguageModel lang : settingsModel.getLanguages()) {
            if (settingsModel.getSelectedLanguageCode().equals(lang.getLanguageCode())) {
                showMessage(frame, client.isConnected() ? 
                    lang.getMainMessageTexts().getOnConnected() : 
                    lang.getMainMessageTexts().getOnDisconnected());
            }
        }
    }

    private void showMessage(JFrame frame, String message) {
        ToastHelper toastHelper = new ToastHelper(message, frame);
        toastHelper.showMessage();
    }

    private void LoadComponents() {
        SettingsModel settingsModel = settingsHelper.LoadSettings();
        for (LanguageModel lang : settingsModel.getLanguages()) {
            if (settingsModel.getSelectedLanguageCode().equals(lang.getLanguageCode())) {
                updateUIComponents(lang, settingsModel);
            }
        }
    }

    private void updateUIComponents(LanguageModel lang, SettingsModel settingsModel) {
        Font customFont = getAppropriateFont(lang);
        
        usernameLabel.setText(lang.getFirstForm().getFrameTitle());
        usernameLabel.setFont(customFont);
        
        themeButton.setText(settingsModel.getIsDarkTheme() ? 
            lang.getFirstForm().getThemeButtonTextDark() : 
            lang.getFirstForm().getThemeButtonTextLight());
        themeButton.setFont(customFont);
        
        langButton.setSelectedItem(lang.getFirstForm().getLangButtonText());
        langButton.setFont(customFont);
        
        nickNameHint.setText(lang.getFirstForm().getNickNameHint());
        nickNameHint.setFont(customFont);
        
        userName.setToolTipText(lang.getFirstForm().getNickNameToolTip());
        clientButton.setText(lang.getFirstForm().getClientButtonText());
        clientButton.setFont(customFont);
        serverButton.setText(lang.getFirstForm().getServerButtonText());
        serverButton.setFont(customFont);
    }

    private Font getAppropriateFont(LanguageModel lang) {
        if (lang.getLanguageCode().equals("per")) {
            try {
                return Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/yekan.ttf")).deriveFont(12f);
            } catch (Exception e) {
                return new Font("Segoe UI", Font.PLAIN, 12);
            }
        }
        return new Font("Segoe UI", Font.PLAIN, 12);
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

    public void showSecondForm(MqttClient client, ClientModel clientModel, boolean isServer, String username) {
        SettingsHelper settingsHelper = new SettingsHelper();
        SettingsModel settingsModel = settingsHelper.LoadSettings();
        for (LanguageModel languageModel : settingsModel.getLanguages()) {
            if (languageModel.getLanguageCode().equals(settingsModel.getSelectedLanguageCode())) {
                new PlayerForm(client, clientModel, isServer, username, languageModel);
            }
        }
    }
}
