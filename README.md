<h1 align="center">🌍 Geohunt</h1>

<p align="center">
  A geography guessing game inspired by GeoGuessr.<br/>
  Explore random locations, analyze the surroundings, and guess where you are.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue" />
  <img src="https://img.shields.io/badge/Architecture-Clean-orange" />
  <img src="https://img.shields.io/badge/Pattern-MVVM-purple" />
</p>

<hr/>

<h2>🎯 About The Project</h2>

<p>
  <b>Geohunt</b> is a location-based guessing game where players are placed in random
  street-level locations and must determine their position by observing environmental clues.
</p>

<p>
  The closer the guess to the actual location, the higher the score.
  This project focuses on modern Android development practices and scalable architecture.
</p>

<hr/>

<h2>📸 Screenshots / Demo</h2>

<p align="center">
  <i>
    <img width="1600" height="800" alt="Group 66 (1)" src="https://github.com/user-attachments/assets/a6bd4176-361c-4622-bfbc-ef32fb1ac1d7" />
  </i>
</p>

<hr/>

<h2>✨ Features</h2>

<ul>
  <li>🌍 Random street view locations</li>
  <li>🧭 Guess location by dropping a pin on the map</li>
  <li>📏 Distance-based scoring</li>
  <li>🗺 Interactive map navigation</li>
  <li>⚡ Reactive UI with StateFlow & SharedFlow</li>
  <li>🏗 Clean and maintainable architecture</li>
</ul>

<hr/>

<h2>🧱 Architecture</h2>

<p>
  This project follows <b>Clean Architecture</b> principles combined with <b>MVVM</b>
  to ensure separation of concerns, testability, and scalability.
</p>

<pre>
Presentation (Jetpack Compose + ViewModel)
        ↓
Domain (Use Cases / Business Logic)
        ↓
Data (Repository / API / Mapper)
</pre>

<p>
  Navigation is handled using <b>Single Activity Architecture</b> with <b>NavHost</b>.
</p>

<hr/>

<h2>🛠 Tech Stack</h2>

<ul>
  <li><b>Kotlin</b></li>
  <li><b>Jetpack Compose</b></li>
  <li><b>Single Activity Architecture</b></li>
  <li><b>Navigation Component (NavHost)</b></li>
  <li><b>Hilt</b> for Dependency Injection</li>
  <li><b>MVVM</b> architecture</li>
  <li><b>StateFlow & SharedFlow</b> for state management</li>
  <li><b>Retrofit</b> for networking</li>
</ul>

<hr/>

<h2>🌐 Street View Provider & Credits</h2>

<p>
  Street-level imagery and related data are provided by <b>Kartaview API</b>.
</p>

<p>
  Kartaview is an open platform for street-level imagery powered by the community.
  Special thanks to Kartaview contributors for making open geospatial data accessible.
</p>

<p>
  If you use this project, please consider supporting Kartaview and their open-data ecosystem.
</p>

<hr/>

<h2>🚀 Getting Started</h2>

<h3>Clone Repository</h3>

<pre>
git clone https://github.com/KEVINGILBERTTODING/GeoHunt.git
</pre>

<p>
  Open the project using Android Studio, configure Gmap API keys at local.properties,
  then build and run the app.
</p>

<hr/>

<h2>🎮 How To Play</h2>

<ol>
  <li>Launch the game</li>
  <li>Observe the street view carefully</li>
  <li>Open the map</li>
  <li>Drop your best guess</li>
  <li>Get your score based on distance accuracy</li>
</ol>

<hr/>

<h2>🤝 Contribution</h2>

<p>
  Contributions, issues, and feature requests are welcome.
  Feel free to fork the repository and submit a pull request.
</p>
