package com.gtera.utils

object APPConstants {

    const val APP_ARABIC_LANGUAGE = "ar"
    const val APP_ENGLISH_LANGUAGE = "en"
    const val ABAZA_SHARED_PREFERENCES = "abaza"

    const val NO_SELECTION = -1

    // Extras
    const val EXTRAS_KEY_FAV = "favorite"
    const val EXTRAS_KEY_COMPARE = "compare"
    const val EXTRAS_KEY_CAR_CATEGORY_SELECTION = "car_category_selection"
    const val EXTRAS_KEY_BRAND = "brand"
    const val EXTRAS_KEY_BRAND_ID = "brand_id"
    const val EXTRAS_KEY_MODEL = "model"
    const val EXTRAS_KEY_MODEL_ID = "model_id"
    const val EXTRAS_KEY_RESET_PASSWORD_MAIL = "reset_mail"
    const val EXTRAS_KEY_PRODUCT = "product"
    const val EXTRAS_KEY_PRODUCTS = "products"
    const val EXTRAS_KEY_CAR_ID = "car_id"
    const val EXTRAS_KEY_CAR_NAME = "car_name"
    const val EXTRAS_KEY_CAR_TYPE = "car_type"
    const val EXTRAS_KEY_NEWS = "news"
    const val EXTRAS_KEY_BUDGET = "budget_id"
    const val EXTRAS_KEY_BUDGET_BRAND_ID = "budget_brand_id"
    const val EXTRAS_KEY_CAR_COMPARE_LIST = "compare_list"
    const val EXTRAS_KEY_CAR_SEARCH_BRANDS_IDS = "car_search_brands_ids"
    const val EXTRAS_KEY_IS_NEW_CARS = "IS_NEW_CARS"
    const val EXTRAS_KEY_CAR_SEARCH_TEXT = "car_search_text"
    const val EXTRAS_KEY_FAVORITE= "favorites"
    const val EXTRAS_KEY_INSURANCE_REQUEST= "insurance_request"
    const val INTENT_DATA = "data"
    const val EXTRAS_KEY_CARS = "cars"
    const val EXTRAS_KEY_BRANCH = "branch"
    const val EXTRAS_KEY_INSURANCE_ID= "insurance_id"
    const val EXTRAS_KEY_WEB_URL= "web_url"
    const val EXTRAS_KEY_PAYMENT_MESSAGE= "payment_message"
    const val EXTRAS_KEY_NEW_ID = "news_id"
    const val EXTRAS_KEY_OFFER_ID = "offer_id"





    // request codes
    const val REQUEST_CODE_FILTER: Int = 99
    const val REQUEST_CODE_COMPARE: Int = 100
    const val REQUEST_CODE_FAVORITE: Int = 101
    const val REQUEST_IMAGE_CAPTURE = 0
    const val REQUEST_TAKE_PHOTO:Int = 1
    const val REQUEST_IMAGE_SECOND_CAPTURE = 2
    const val REQUEST_TAKE_SECOND_PHOTO:Int = 3
    const val REQUEST_CODE_ONLINE_PAYMENT = 102
    const val RESULT_CODE_ERROR = -1


    // Attributes value types
    const val ATTRIBUTE_VALUE_STRING = "1"
    const val ATTRIBUTE_VALUE_INT = "1"
    const val ATTRIBUTE_VALUE_BOOLEAN = "0"

    const val CONSTANT_SEARCH_SIZE = 2
    const val CONSTANT_PAGING_PER_PAGE_NUM = 15

    // Cars Sort  values
    const val CAR_SORT_BY_CREATED_AT = "created_at"
    const val CAR_SORT_BY_PRICE = "price_from"
    const val TOP_DEALS_SORT_BY_PRICE = "price"
    const val DESC = 1
    const val ASC = 0


    // socialMedia Types
    const val SOCIAL_MEDIA_FACEBOOK_TYPE = "facebook"
    const val SOCIAL_MEDIA_GOOGLE_TYPE = "google"

    // favorites Types
    const val FAVORITE_CAR_TYPE = "car"
    const val FAVORITE_OFFER_TYPE = "offer"

    //Filter Options Keys
    const val FILTER_OPTION_RESULT = "filter_result"
    const val FILTER_OPTION_CATEGORIES = "category_id"
    const val FILTER_OPTION_CAR_MODELS = "model_id"
    const val FILTER_OPTION_BODY_TYPE = "body_type_id"
    const val FILTER_OPTION_TRANSMISSION_TYPE = "transmission_type_id"
    const val FILTER_OPTION_PULL_TYPE = "pull_type_id"
    const val FILTER_OPTION_STEERING_WHEEL_TYPE = "steering_wheel_type_id"
    const val FILTER_OPTION_COLORS = "color_id"
    const val FILTER_OPTION_HAS = "has"
    const val FILTER_OPTION_PRICE = "price"
    const val FILTER_OPTION_MANUFACTURE_YEAR = "manufacture_year"

    //Filter options viewTypes
    const val FILTER_TYPE_COLOR = "color"
    const val FILTER_TYPE_GRID = "grid"
    const val FILTER_TYPE_HORIZONTAL = "horizontal"
    const val FILTER_TYPE_ATTRIBUTES = "attributes"
    const val FILTER_TYPE_DROWN_DOWN = "drowpDown"
    const val FILTER_TYPE_RANG = "range"
    const val FILTER_TYPE_MIN_MAX = "minMax"

    //for branches
    val REQUEST_CODE_LOCATION_PERMISSION = 300
    val REQUEST_CODE_STORAGE_PERMISSION = 301

    //slidet types
    const val SLIDER_TYPE_NEWS = "news"
    const val SLIDER_TYPE_CAR = "car"
    const val SLIDER_TYPE_OFFER = "offer"

    //Selector
    const val SELECTOR_HEADER_TEXT = "selector_header_text"
    const val SELECTOR_SEARCH_HINT = "selector_header_text"
    const val SELECTOR_OTHER_TEXT = "selector_other_text"
    const val SELECTOR_TYPE = "selector_type"
    const val SELECTOR_BRAND = "brand"
    const val SELECTOR_BRAND_ID = "brand_id"
    const val SELECTOR_BRAND_BRAND_NAME = "brand_name"
    const val SELECTOR_MODEL = "model"
    const val SELECTOR_MODEL_ID = "model_id"
    const val SELECTOR_MODEL_NAME = "model_name"
    const val SELECTOR_YEAR = "year"
    const val SELECTOR_MIN_YEAR = 1970

    // add my car
    const val SELECTED_CAR_NAME_AND_MODEL_AND_YEAR = "carSelectedData"
    const val IS_MY_CAR = "is_my_car"

    // clipData
    const val CLIP_DATA = "clipData"

    // Messages
    const val MESSAGE_ID = "clipData"
    const val MESSAGE_USER_IMAGE = "messageUserImage"
    const val MESSAGE_FROM = "from"
    const val MESSAGE_TO = "to"

    // Order now
    const val ORDER_NOW_CAR = "car"
    const val ORDER_NOW_CAR_TYPE = "car_type"
    const val ORDER_NOW_CAR_NAME = "car_name"
    const val ORDER_NOW_CAR_MONTHLY_PAYMENT = "monthly_payment"
    const val ORDER_NOW_CAR_ORDER = "order"
    const val ORDER_NOW_ATTORNEY_PERSON = "attorneyPerson"
    const val ORDER_NOW_BRANCH = "branch"
    const val CASH = 1
    const val PREMIUM = 2

    // Offers
    const val OFFER_PERCENTAGE = "percentage"
    const val EXTRAS_KEY_CATEGORY = "category"



}