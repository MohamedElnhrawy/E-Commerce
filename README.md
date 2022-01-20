# E-Commerce :partly_sunny:

## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.


## Technologies used:

* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Material Design](https://material.io/develop/android/docs/getting-started/) an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to declaratively bind UI components in layouts to data sources.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Android KTX](https://developer.android.com/kotlin/ktx) which helps to write more concise, idiomatic Kotlin code.
* [Dagger2](https://developer.android.com/training/dependency-injection/dagger-android) which helps to write more readable,usable and testable.
* [Firebase Services](https://firebase.google.com/docs?authuser=0) which helps to sign in with email and phone.
* [Deep Links](https://developer.android.com/training/app-links/deep-linking) which helps to apply promotions deep linking for cart.


## Installation
Auth:
     install app login with preferred sign in method [phone , email&pass] :
        - phone method will accept  Egyptian numbers only for simple validation, A verification otp will be sent with 6 digits
                to verify that phone, then logged in user saved locally to app then you get authorized to enter the app.
        - email sign in will create account if not exists and send verification email else will sign in directly.
Home Flow:
        - home screen have welcome tile with available info from firebase Auth user returned from signed in process, if not null
                you will see [name else email else phone].
        - dynamic vertical list of Categories consists of horizontal list of products with see all action.
            note: categories all accessed locally from json file called [home.txt].

        - clicking any product will show full pro details screen with option to add to cart, cart is room local database

Shopping List (cart):
        _ adding to cart will update home view with a new horizontal cat items list with see all cart product action or from
            profile.

        - cart screen navigation from home or profile with checkout action that empty cart only

        - Promotion supported for non empty cart with some queries and standard format according to promotion requirements
         *[example]:[https://www.ecommerce-app.com/promotion?code=200&start=2022-01-20&end=2022-01-28&min=1&max=20&exclude=School&amount=50]

Products List:
        - search performed in local json file called [search.txt]
        - screen with search view to search only products not categories.
        - swipe refresh option
        - click action to see full product details screen.

Profile screen:
        - 2 options for :
           - showing cart.
           - logout [locally and (remote -> firebase)]



[Note]: SMS Verification only valid for only 50 request per day.


Copyright (c) 2022 Mohamed Alnahrawy.

