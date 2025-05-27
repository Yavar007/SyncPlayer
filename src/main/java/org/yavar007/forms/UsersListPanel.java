package org.yavar007.forms;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import org.yavar007.database.RoomDatabaseHelper;
import org.yavar007.models.LanguageModels.SecondFormLanguageModel;
import org.yavar007.models.UsersInRoomModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


public class UsersListPanel {
    private final JWindow window;
    private final DefaultListModel<UsersInRoomModel> listModel;
    private final JList<UsersInRoomModel> userList;
    private final RoomDatabaseHelper roomDatabaseHelper;
    private final JLabel usersCountLabel;
    private final JFrame frame;
    private final boolean isServer;
    private final SecondFormLanguageModel secondFormLanguageModel;

    public UsersListPanel(boolean isServer,JFrame holder, SecondFormLanguageModel secondFormLanguageModel) {
        window = new JWindow();
        this.frame=holder;
        this.isServer=isServer;
        this.secondFormLanguageModel=secondFormLanguageModel;
        roomDatabaseHelper = new RoomDatabaseHelper();
        List<UsersInRoomModel> users = roomDatabaseHelper.selectAllData();

        // Initialize the list model and JList
        listModel = new DefaultListModel<>();
        for (UsersInRoomModel user : users) {
            listModel.addElement(user);
        }
        userList = new JList<>(listModel);

        // Custom cell renderer
        userList.setCellRenderer((list, user, _, isSelected, cellHasFocus) -> {
            JPanel userPanel = new JPanel(new GridBagLayout());
            userPanel.setPreferredSize(new Dimension(200, 50));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            JLabel image = new JLabel(loadImage(user.getOSName()));
            JLabel userName = new JLabel(user.getNickName());
            userName.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel roomUserIndicator = new JLabel();
            roomUserIndicator.setPreferredSize(new Dimension(10, 40));
            roomUserIndicator.setIcon(createColoredIcon(10, 40,
                    user.getClientRole().equals("server") ? new Color(255, 215, 0) : new Color(142, 142, 142)));
            JLabel onlineStats = new JLabel();
            onlineStats.setPreferredSize(new Dimension(10, 40));
            onlineStats.setIcon(createColoredIcon(10, 40,
                    user.isOnline() ? new Color(26, 255, 0) : new Color(255, 0, 0)));
            JPanel userStatusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            userStatusPanel.add(roomUserIndicator);
            userStatusPanel.add(onlineStats);
            gbc.gridx = 0;
            gbc.gridy = 0;
            userPanel.add(image, gbc);
            gbc.gridx = 1;
            userPanel.add(userStatusPanel, gbc);
            gbc.gridx = 3;
            gbc.weightx = 0.5;
            userPanel.add(userName, gbc);

            if (isSelected) {
                userPanel.setBackground(userList.getSelectionBackground());
            } else {
                userPanel.setBackground(userList.getBackground());
            }
            return userPanel;
        });

        // Panel layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(userList);
        usersCountLabel = new JLabel(secondFormLanguageModel.getUserListTexts().formatted(isServer?(users.size() - 1):users.size()));
        usersCountLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        usersCountLabel.setBackground(Color.GRAY);
        usersCountLabel.setPreferredSize(new Dimension(window.getWidth(), 40));
        usersCountLabel.setOpaque(true);
        mainPanel.add(usersCountLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        window.add(mainPanel);

    }
    public void openUsersList(boolean isRTL) {
        if (!window.isVisible()) {
            // Set the size of the window before calculating its position
            window.setSize(220, frame.getHeight() - 30);

            int x;
            if (isRTL) {
                // Open from the right side in RTL mode
                x = frame.getX() + frame.getWidth() - window.getWidth();
            } else {
                // Open from the left side in LTR mode
                x = frame.getX();
            }

            // Set the location of the window based on the direction
            window.setLocation(x, frame.getY() + 30);

            // Set opacity and visibility
            window.setOpacity(1);
            window.setVisible(true);
        }
    }


    public void closeUsersList() {
        if (window.isVisible()) {
            window.setVisible(false);
        }
    }

    public void updateUsers() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            List<UsersInRoomModel> users = roomDatabaseHelper.selectAllData();
            for (UsersInRoomModel user : users) {
                listModel.addElement(user);
            }
            if (isServer){
                usersCountLabel.setText(secondFormLanguageModel.getUserListTexts().formatted(users.size()-1));
            }else{
                usersCountLabel.setText(secondFormLanguageModel.getUserListTexts().formatted(users.size()));
            }
        });
    }

    private ImageIcon loadImage(String imageName) {
        try {

            String imagePath = getImagePath(imageName);

            // Resize the image to 40x40 with high quality using Thumbnailator

            BufferedImage img = Thumbnails.of(new File(imagePath))
                    .size(40, 40).keepAspectRatio(false).antialiasing(Antialiasing.ON)
                    .asBufferedImage();

            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null; // Return null if the image cannot be loaded
        }
    }

    private static String getImagePath(String imageName) {
        String imagePath = "images/";
        if (imageName.toLowerCase().contains("windows")) {
            imagePath += "windows.png";
        } else if (imageName.toLowerCase().contains("mac")) {
            imagePath += "macos.png";
        } else if (imageName.toLowerCase().contains("linux")) {
            imagePath += "linux.png";
        } else if (imageName.toLowerCase().contains("android")) {
            imagePath += "android.png";
        } else {
            // Default image if no OS matches
            imagePath += "windows.png";
        }
        return imagePath;
    }

    private ImageIcon createColoredIcon(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return new ImageIcon(image);
    }
}

