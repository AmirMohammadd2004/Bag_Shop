# BagShop Android App 👜

![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-orange?style=flat-square)
![Build](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)
![Last Commit](https://img.shields.io/github/last-commit/username/BagShop?style=flat-square)

BagShop یک اپلیکیشن فروشگاه آنلاین برای خرید کیف و محصولات مشابه است، با امکانات سبد خرید، ثبت سفارش، پرداخت، نظردهی و مدیریت کاربران.

---

## 📌 ویژگی‌ها

- مرور و جستجوی محصولات
- اضافه و حذف محصول از سبد خرید
- ثبت سفارش و پرداخت آنلاین
- مدیریت پروفایل کاربری
- ارسال و مشاهده نظرات کاربران
- نمایش تبلیغات و بنرهای محصولات

---

## 🛠 تکنولوژی‌ها و ابزارها

- **زبان برنامه‌نویسی:** Kotlin  
- **معماری:** MVVM (Model-View-ViewModel)  
- **شبکه:** Retrofit + OkHttp + Gson  
- **دیتابیس محلی:** Room  
- **Dependency Injection:** Koin  
- **ذخیره‌سازی ساده:** SharedPreferences  
- **Coroutine:** عملیات غیرهمزمان و شبکه‌ای  
- **UI:** Jetpack Compose / XML

---

## 🏗️ ساختار پروژه
com.amir.bagshop
│
├─ model
│ ├─ data // دیتا مدل‌ها (Product, UserCartInfo, SubmitOrder, ...)
│ ├─ net // ApiService و Retrofit setup
│ └─ repository // Repositories (Product, User, Cart, Comments)
│
├─ ui
│ └─ features
│ ├─ Main
│ ├─ Sign_In
│ ├─ Sign_Up
│ ├─ Cart
│ ├─ Product
│ ├─ Category
│ └─ Profile
│
└─ di // Koin Modules

🌐 تنظیمات سرور

BASE_URL در ApiService باید به آدرس سرور REST API اشاره کند.

Endpoint ها:

/signUp, /signIn → کاربران

/getProducts, /getSliderPics → محصولات و تبلیغات

/addToCart, /removeFromCart, /getUserCart → سبد خرید

/submitOrder, /checkout → پرداخت و سفارش

