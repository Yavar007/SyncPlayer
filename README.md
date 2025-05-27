
---

# **SyncPlayer: Synchronized Online Video Streaming for Desktop and Android**

<div style="text-align: center;">
  <img src="applogo.png" alt="SyncPlayer Logo" width="300">
</div>

SyncPlayer is a **Java-based Desktop and Android application** that enables real-time synchronized **Online Video Streaming** for multiple users. Whether you're hosting a movie night, conducting a team review, or simply watching videos with friends, SyncPlayer ensures everyone stays in perfect sync. With support for **Players (Projectionists)** and **Spectators**, customizable themes, multi-language support, and **Secure AES Encryption**, SyncPlayer offers a seamless and immersive viewing experience.

---

## **Features**

- **Real-Time Synchronization**: Uses the **MQTTV protocol** for seamless communication between users.
- **Role-Based Access**:
  - **Projectionist (Player)**: Controls video playback, inputs the **direct online streaming link**, and generates a unique room ID.
  - **Spectator**: Joins the room using the room ID and watches the video in sync.
- **Secure Communication**: Data is encrypted using **AES/CBC/PKCS5PADDING** with a customizable secret key for enhanced security.
- **Multi-Platform Support**: Available for **Desktop (Java)** and **Android**(Soon...).
- **Customizable Themes**: Light and dark themes for personalized viewing.
- **Multi-Language Support**: Supports **English** and **Persian** out of the box, with the ability for desktop users to add custom languages.
- **User-Friendly Interface**:
  - Two forms: 
    1. **Init Form**: Choose language, theme, nickname, and role (Projectionist or Spectator).
    2. **Player Form**: Input the **direct online streaming link** (Projectionist) or room ID (Spectator) and control playback.
  - Fullscreen support, play/pause/stop controls, and subtitle/language selection (if available in the video stream).
- **Room Management**: Sidebar displays all users in the room (both Projectionists and Spectators).
- **Custom Toast Notifications**: Notifications for user join/leave events, room ID copied, and more.
- **No VPN Required for Iranian Users**: Optimized for accessibility in Iran. if video url does not requires any VPN...

---

## **How It Works**

1. **Choose Preferences**: On the first form, users select their preferred language, theme, nickname, and role (Projectionist or Spectator).
2. **Create or Join a Room**:
   - **Projectionist**: Inputs a **direct online streaming link**, and a random room ID is generated. The room ID can be copied to the clipboard and shared.
   - **Spectator**: Inputs the room ID to join the session.
3. **Sync and Play**: Once the room is active, all users experience synchronized video streaming.
4. **Interact**: Use the sidebar to see who’s in the room and enjoy the movie together!

---

## **Technologies Used**

- **Programming Language**: Java
- **Communication Protocol**: MQTTV
- **Encryption**: **AES/CBC/PKCS5PADDING** with a customizable secret key for secure data transfer.
- **Platforms**: Desktop (Java) and Android (Soon...)
- **UI/UX**: Customizable themes, multi-language support, and intuitive controls

---

## **Installation**

### **Desktop Version**
1. **Download the Zip File**: Download the latest release from the [Releases](https://github.com/Yavar007/SyncPlayer/releases/) section.
2. **Extract Everything**
3. **Run the Application** You can run it directly using this command (if you have VLC installed in its Default Directory):
   ```bash
   java -jar SyncPlayer.jar
   ```
or you can use .bat or .sh files to run the program based on your operation system.
you can use 
3. **Follow On-Screen Instructions**: Choose your preferences and start using SyncPlayer.

---

## **Usage**

1. **Init Form**:
   - Select language (English or Persian).
   - Choose theme (Light or Dark).
   - Enter a nickname.
   - Select your role (Projectionist or Spectator).

2. **Player Form**:
   - **Projectionist**:
     - Input the **direct online streaming link**.
     - Copy the generated room ID and share it with others.
     - Click "Play" to start the video.
   - **Spectator**:
     - Input the room ID provided by the Projectionist.
     - Click "Join" to sync with the room.

3. **Control Playback**:
   - Use play, pause, stop, and seek controls.
   - Select subtitles or audio language (if available in the video stream).
   - Switch to fullscreen for an immersive experience.

4. **Room Management**:
   - View all users in the room via the sidebar.
   - Receive notifications for user join/leave events and other actions.

5. **Custom Encryption Key**:
   - Users can customize the **AES secret key** for enhanced security.

---

## **Screenshots**
**Init Form**
<div style="text-align: center;">
  <img src="/screenshots/en-light-ff.png" alt="FirstForm_Light" width="300">
  <img src="/screenshots/en-dark-ff.png" alt="FirstForm_Dark" width="300">
</div>

<br/>

**Player Form**

<div style="text-align: center;">
  <img src="/screenshots/en-light-sf-pr.png" alt="SecondForm_Light" width="300">
  <img src="/screenshots/en-dark-sf-pr.png" alt="SecondForm_Dark" width="300">
  <img src="/screenshots/en-light-sf-pro.png" alt="SecondForm_Users_List_Light" width="300">
  <img src="/screenshots/en-dark-sf-pro.png" alt="SecondForm_Users_List_Dark" width="300">
  <img src="/screenshots/en-light-sf-sp.png" alt="SecondForm_Spectator_Light" width="300">
  <img src="/screenshots/en-dark-sf-sp.png" alt="SecondForm_Spectator_Dark" width="300">
</div>



---

## **Contact**

For questions, feedback, or support, feel free to reach out:

- **Email**: yavar007@live.com
- **LinkedIn**: [Yavar007](https://www.linkedin.com/in/yavar-qalichi-966708274)

---

**Enjoy Watching Movies with Your Friends!** 🎥

---
