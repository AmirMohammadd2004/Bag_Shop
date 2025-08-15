# BagShop Android App 👜

BagShop is an online shopping application for bags and related products, offering features like cart management, order submission, online payment, user comments, and profile management.

---

## 📌 Features

- Browse and search products
- Add or remove products from the cart
- Submit orders and online payments
- User profile management
- Submit and view user comments
- Display ads and product banners

---

## 🛠 Technologies

- **Kotlin 2.2.0** with **Jetpack Compose**
- **Kotlin Coroutines (Core & Android) 1.10.2** for asynchronous programming
- **Android Gradle Plugin 8.9.2**
- **MVVM Architecture** (ViewModel, Repository)
- **Room** for local database
- **Retrofit & Gson Converter** for networking
- **Parse SDK** for backend integration
- **Coil** for image loading
- **Lottie Compose** for animations
- **Cokoin** for dependency injection
- **Accompanist System UI Controller**
- **JUnit & Espresso** for testing

---

## 🏗 Architecture

- **UI:** Jetpack Compose
- **ViewModel:** Manages state and interacts with repositories
- **Repository:** Handles data from network and local database
- **ApiService:** Retrofit-based network layer
- **Room:** Local caching and persistent storage
- **Cokoin:** Dependency injection
- **Coroutines:** Asynchronous operations (network calls, database operations)

---

## 📸 Screenshots

![Home Screen](assets/images/home.png)  
![Product Screen](assets/images/product.png)  
![Cart Screen](assets/images/cart.png)  
![Profile Screen](assets/images/profile.png)  

---

## 🎬 Demo

![App Demo](assets/images/demo.gif)  

> Tip: Use a short 5–10 second GIF showing main flows (browsing, adding to cart, checking out) for best GitHub readability.
