# Project Structure Documentation

## Project Overview

**Project Name:** project213.2 (Kotlin E-Commerce Application)  
**Package Name:** com.uilover.project2132  
**Type:** Android E-Commerce Mobile Application  
**Language:** Kotlin  
**UI Framework:** Jetpack Compose  
**Backend:** Firebase Realtime Database  

---

## Table of Contents
1. [Project Information](#project-information)
2. [Architecture Overview](#architecture-overview)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Key Components](#key-components)
6. [Firebase Integration](#firebase-integration)
7. [Dependencies](#dependencies)
8. [Build Configuration](#build-configuration)

---

## Project Information

### Build Configuration
- **Compile SDK:** 35
- **Target SDK:** 34
- **Min SDK:** 24
- **Version Code:** 1
- **Version Name:** 1.0
- **Java Version:** 11
- **Kotlin Version:** 2.0.0
- **Android Gradle Plugin:** 8.7.2
- **Compose Kotlin Compiler Extension:** 1.5.1

### Application ID
`com.uilover.project2132`

### Firebase Configuration
- **Project ID:** minshop-2972a
- **Project Number:** 341631306530
- **Firebase URL:** https://minshop-2972a-default-rtdb.asia-southeast1.firebasedatabase.app
- **Storage Bucket:** minshop-2972a.firebasestorage.app

**Note:** ✅ **google-services.json** file **EXISTS** in the project at `app/google-services.json`

**Note:** ✅ **Google Services Plugin** (`com.google.gms.google-services`) **IS CONFIGURED** in `app/build.gradle.kts` at line 5:
```kotlin
alias(libs.plugins.google.gms.google.services)
```

---

## Architecture Overview

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:

```
┌─────────────────────────────────────────────┐
│            Activity (View Layer)            │
│        Jetpack Compose UI Components        │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│         ViewModel (Business Logic)          │
│         Manages UI State & Events           │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│       Repository (Data Layer)               │
│    Firebase Database Operations             │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│      Model (Data Classes)                   │
│   ItemsModel, CategoryModel, SliderModel    │
└─────────────────────────────────────────────┘
```

---

## Technology Stack

### Core Technologies
- **Kotlin:** Modern programming language for Android
- **Jetpack Compose:** Declarative UI framework
- **Firebase Realtime Database:** Cloud-hosted NoSQL database
- **LiveData:** Observable data holder for lifecycle-aware data management
- **ViewModel:** Manages UI-related data in a lifecycle-conscious way

### UI Components
- **Material3:** Material Design 3 components
- **Accompanist Pager:** Horizontal pager and indicators for Compose
- **Coil:** Image loading library for Compose
- **ConstraintLayout Compose:** Complex layouts in Compose

### Additional Libraries
- **Glide:** Image loading and caching
- **Gson:** JSON serialization/deserialization
- **TinyDB:** SharedPreferences helper for cart management

---

## Project Structure

```
E:\Kotlin_woocomerce/
│
├── app/
│   ├── build.gradle.kts                    # App-level build configuration
│   ├── google-services.json                # Firebase configuration file
│   ├── proguard-rules.pro                  # ProGuard configuration
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml         # App manifest
│   │   │   │
│   │   │   ├── java/com/uilover/project2132/
│   │   │   │   │
│   │   │   │   ├── Activity/               # Activity Layer (UI Screens)
│   │   │   │   │   ├── BaseActivity.kt
│   │   │   │   │   ├── IntroActivity.kt     # Entry/Splash screen
│   │   │   │   │   ├── MainActivity.kt      # Main home screen
│   │   │   │   │   ├── ListItemsActivity.kt # Product listing by category
│   │   │   │   │   ├── ListItems.kt
│   │   │   │   │   ├── DetailActivity.kt    # Product detail screen
│   │   │   │   │   └── CartActivity.kt      # Shopping cart screen
│   │   │   │   │
│   │   │   │   ├── ViewModel/              # ViewModel Layer
│   │   │   │   │   └── MainViewModel.kt     # Main business logic
│   │   │   │   │
│   │   │   │   ├── Repository/             # Data Layer
│   │   │   │   │   └── MainRepository.kt    # Firebase data operations
│   │   │   │   │
│   │   │   │   ├── Model/                  # Data Models
│   │   │   │   │   ├── ItemsModel.kt        # Product data model
│   │   │   │   │   ├── CategoryModel.kt     # Category data model
│   │   │   │   │   └── SliderModel.kt       # Banner slider data model
│   │   │   │   │
│   │   │   │   ├── Helper/                 # Helper Classes
│   │   │   │   │   ├── ManagmentCart.kt     # Cart management logic
│   │   │   │   │   └── ChangeNumberItemsListener.kt
│   │   │   │   │
│   │   │   │   └── ui/theme/               # UI Theme Configuration
│   │   │   │       ├── Theme.kt             # App theme
│   │   │   │       ├── Color.kt             # Color palette
│   │   │   │       └── Type.kt              # Typography
│   │   │   │
│   │   │   └── res/                        # Resources
│   │   │       ├── drawable/               # Drawable resources
│   │   │       ├── mipmap/                 # App icons
│   │   │       ├── values/                 # Values (strings, colors, etc.)
│   │   │       └── xml/                    # XML configurations
│   │   │
│   │   ├── androidTest/                    # Android instrumented tests
│   │   └── test/                           # Unit tests
│   │
│   └── build/                              # Build outputs (generated)
│
├── gradle/
│   ├── libs.versions.toml                  # Version catalog
│   └── wrapper/                            # Gradle wrapper files
│
├── build.gradle.kts                        # Project-level build config
├── settings.gradle.kts                     # Project settings
├── gradle.properties                       # Gradle properties
├── gradlew                                 # Gradle wrapper (Unix)
├── gradlew.bat                             # Gradle wrapper (Windows)
└── local.properties                        # Local SDK configuration
```

---

## Key Components

### 1. Activities (UI Layer)

#### **IntroActivity**
- **Purpose:** Entry/Splash screen
- **Launch Activity:** First activity shown when app starts
- **Features:** 
  - Displays app background/logo
  - Navigates to MainActivity on click

#### **MainActivity**
- **Purpose:** Main home screen
- **Features:**
  - Banner slider (auto-scrolling promotional images)
  - Category grid/list
  - Popular/Recommended products
  - Navigation to product listing and cart
- **UI:** Fully built with Jetpack Compose

#### **ListItemsActivity**
- **Purpose:** Display products filtered by category
- **Features:**
  - Shows products for selected category
  - Product grid/list view
  - Navigation to product details

#### **DetailActivity**
- **Purpose:** Product detail screen
- **Features:**
  - Product images gallery
  - Product description
  - Price and rating
  - Model/variant selection
  - Add to cart functionality

#### **CartActivity**
- **Purpose:** Shopping cart management
- **Features:**
  - List of cart items
  - Quantity adjustment (+/-)
  - Remove items
  - Total price calculation
  - Checkout functionality

#### **BaseActivity**
- **Purpose:** Base activity with common functionality
- **Usage:** Parent class for other activities

---

### 2. ViewModels

#### **MainViewModel**
- **Purpose:** Manages business logic for main screen
- **Methods:**
  - `loadBanner()`: Fetches banner images from Firebase
  - `loadCategory()`: Fetches product categories
  - `loadPopular()`: Fetches popular/recommended products
  - `loadFiltered(id)`: Fetches products by category ID
- **Returns:** LiveData objects for reactive UI updates

---

### 3. Repository

#### **MainRepository**
- **Purpose:** Data layer - handles all Firebase operations
- **Database Reference:** Firebase Realtime Database
- **Operations:**
  - Read banner data from "Banner" node
  - Read category data from database
  - Read popular products
  - Filter products by category
- **Pattern:** Repository pattern for clean architecture

---

### 4. Models (Data Classes)

#### **ItemsModel**
```kotlin
- title: String              // Product name
- description: String        // Product description
- picUrl: ArrayList<String>  // Product images URLs
- model: ArrayList<String>   // Product variants/models
- price: Double              // Product price
- rating: Double             // Product rating
- numberInCart: Int          // Quantity in cart
- showRecommended: Boolean   // Featured flag
- categoryId: String         // Category reference
```

#### **CategoryModel**
```kotlin
- title: String              // Category name
- id: Int                    // Category ID
- picUrl: String             // Category icon/image URL
```

#### **SliderModel**
```kotlin
- url: String                // Banner image URL
```

---

### 5. Helpers

#### **ManagmentCart**
- **Purpose:** Shopping cart management
- **Storage:** TinyDB (SharedPreferences wrapper)
- **Methods:**
  - `insertItem(item)`: Add product to cart
  - `getListCart()`: Retrieve all cart items
  - `minusItem()`: Decrease item quantity
  - `plusItem()`: Increase item quantity
  - `getTotalFee()`: Calculate total cart value

#### **ChangeNumberItemsListener**
- **Purpose:** Callback interface for cart quantity changes
- **Usage:** Update UI when cart items are modified

---

### 6. UI Theme

#### **Theme.kt**
- Material3 theme configuration
- Dynamic color support (Android 12+)
- Dark/Light theme support
- Color schemes: DarkColorScheme, LightColorScheme

#### **Color.kt**
- Color palette definitions
- Primary, secondary, tertiary colors

#### **Type.kt**
- Typography configuration
- Font styles and sizes

---

## Firebase Integration

### Database Structure (Expected)
```
Firebase Realtime Database
│
├── Banner/
│   ├── {banner_id}
│   │   └── url: "image_url"
│
├── Category/
│   ├── {category_id}
│   │   ├── title: "Category Name"
│   │   ├── id: 1
│   │   └── picUrl: "icon_url"
│
└── Items/
    ├── {item_id}
    │   ├── title: "Product Name"
    │   ├── description: "Description"
    │   ├── picUrl: ["url1", "url2"]
    │   ├── model: ["model1", "model2"]
    │   ├── price: 99.99
    │   ├── rating: 4.5
    │   ├── showRecommended: true
    │   └── categoryId: "category_id"
```

### Firebase Services Used
- **Firebase Realtime Database:** Product catalog, categories, banners
- **Firebase Storage (configured):** Image hosting (via storage_bucket)

### Security Note
⚠️ **API Key Exposed:** The `google-services.json` file contains sensitive API keys. In production:
- Add `google-services.json` to `.gitignore`
- Use Firebase App Check for security
- Configure Firebase security rules

---

## Dependencies

### Core Android Dependencies
```kotlin
- androidx.core:core-ktx:1.15.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.1
- androidx.activity:activity-compose:1.9.3
- androidx.appcompat:appcompat:1.7.0
```

### Jetpack Compose
```kotlin
- androidx.compose.bom:2024.04.01
- androidx.compose.ui:ui
- androidx.compose.ui:ui-graphics
- androidx.compose.ui:ui-tooling-preview
- androidx.compose.material3:material3
- androidx.compose.runtime:runtime-livedata:1.7.5
- androidx.compose.foundation:foundation:1.5.0
```

### Firebase
```kotlin
- com.google.firebase:firebase-database:21.0.0
- com.google.gms:google-services:4.4.2 (plugin)
```

### Image Loading
```kotlin
- io.coil-kt:coil-compose:2.2.2
- com.github.bumptech.glide:glide:4.13.0
```

### UI Components
```kotlin
- com.google.accompanist:accompanist-pager:0.28.0
- com.google.accompanist:accompanist-pager-indicators:0.28.0
- androidx.constraintlayout:constraintlayout-compose:1.1.0
- com.google.android.material:material:1.12.0
```

### Utilities
```kotlin
- com.google.code.gson:gson:2.10.1
```

### Testing
```kotlin
- junit:junit:4.13.2
- androidx.test.ext:junit:1.2.1
- androidx.test.espresso:espresso-core:3.6.1
```

---

## Build Configuration

### Gradle Files Structure

#### **build.gradle.kts (Project Level)**
- Defines plugins applied to all modules
- Uses version catalog (`libs.versions.toml`)

#### **build.gradle.kts (App Level)**
- Application-specific configuration
- Dependencies declaration
- Build types (debug/release)
- Compile options and Kotlin settings
- Compose configuration

#### **libs.versions.toml**
- Centralized dependency version management
- Defines versions, libraries, and plugins
- Easy version updates across modules

#### **settings.gradle.kts**
- Project name configuration
- Module inclusion (app module)
- Repository configuration (Google, Maven Central)

---

## App Flow

### User Journey
```
1. IntroActivity (Splash Screen)
   ↓
2. MainActivity (Home)
   - View banners
   - Browse categories
   - See popular products
   ↓
3. ListItemsActivity (Category Products)
   - Products filtered by category
   ↓
4. DetailActivity (Product Details)
   - View product info
   - Select model/variant
   - Add to cart
   ↓
5. CartActivity (Shopping Cart)
   - Review cart items
   - Adjust quantities
   - Checkout (to be implemented)
```

---

## Features Implemented

✅ **Firebase Integration**
✅ **Product Catalog**
✅ **Category Browsing**
✅ **Banner Slider**
✅ **Product Details**
✅ **Shopping Cart**
✅ **Cart Persistence (TinyDB)**
✅ **Jetpack Compose UI**
✅ **MVVM Architecture**
✅ **Image Loading (Coil & Glide)**
✅ **Material3 Design**
✅ **Dark/Light Theme Support**

---

## Development Notes

### ProGuard
- ProGuard rules defined in `proguard-rules.pro`
- Currently disabled in debug and release builds (`isMinifyEnabled = false`)

### Testing
- Unit tests in `src/test/`
- Instrumented tests in `src/androidTest/`
- Test runner: AndroidJUnitRunner

### Build Variants
- **Debug:** Development build
- **Release:** Production build (requires signing configuration)

---

## Google Services Configuration

### Answer to Your Question:
**Q: Does the project have google-services.json file?**  
**A: ✅ YES** - The file exists at `E:\Kotlin_woocomerce\app\google-services.json`

**Q: Check com.google.gms.google-services in app/build.gradle**  
**A: ✅ CONFIRMED** - The Google Services plugin is properly configured:

**Location:** `app/build.gradle.kts`, Line 5
```kotlin
alias(libs.plugins.google.gms.google.services)
```

**Plugin Definition:** `gradle/libs.versions.toml`, Line 42
```kotlin
google-gms-google-services = { id = "com.google.gms.google-services", version.ref = "googleGmsGoogleServices" }
```

**Version:** 4.4.2 (defined in line 11 of libs.versions.toml)

---

## Potential Improvements

### Security
- [ ] Add `.gitignore` for `google-services.json`
- [ ] Implement Firebase Authentication
- [ ] Configure Firebase Security Rules
- [ ] Add Firebase App Check

### Features
- [ ] User Authentication/Login
- [ ] Payment Integration
- [ ] Order History
- [ ] Product Search
- [ ] Wishlist/Favorites
- [ ] User Reviews & Ratings
- [ ] Push Notifications
- [ ] Product Recommendations

### Code Quality
- [ ] Add more unit tests
- [ ] Add UI tests
- [ ] Implement error handling
- [ ] Add loading states
- [ ] Implement offline support
- [ ] Add analytics

---

## Contact & Support

**Package:** com.uilover.project2132  
**Firebase Project:** minshop-2972a  
**Last Updated:** December 26, 2025  

---

*This documentation was automatically generated by analyzing the project structure and source code.*

