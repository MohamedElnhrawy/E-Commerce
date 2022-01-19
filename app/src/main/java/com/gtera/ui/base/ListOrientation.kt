package com.gtera.ui.base

enum class ListOrientation {
    ORIENTATION_VERTICAL,  //0
    ORIENTATION_HORIZONTAL,  //1
    ORIENTATION_GRID, //2
    ORIENTATION_HOME //3
}

enum class CarMediaType {
    CAR_IMAGE,
    CAR_VIDEO
}

enum class CarType {
    CAR_LIST,
    CAR_GRID,
    CAR_BUDGET_LIST,
    CAR_SEARCH_RELATED
}

enum class CarCompareItemType {
    IMAGE,
    NAME,
    VALUE
}

enum class FavoriteType {
    NEW_CAR,
    USED_CAR,
    CAR,
    OFFER,
    TOP_DEAL
}

enum class InsuranceStatus(val status: String) {
    PENDING("1"),
    CONFIRMED("2"),
    CANCELLED("3"),
    PAID("4")
}

enum class FilterType (val type: String) {
    FILTER_TYPE_DEFAULT(""),
    FILTER_TYPE_GRID("0"),
    FILTER_TYPE_HORIZONTAL("1"),
    FILTER_TYPE_RANG("2"),
    FILTER_TYPE_DROWN_DOWN("3"),
    FILTER_TYPE_MIN_MAX("4"),
    FILTER_TYPE_ATTRIBUTES("5"),
    FILTER_TYPE_HORIZONTAL_COLOR("6")


}

enum class SelectorType {
    BRAND,
    MODEL,
    YEAR    //  and so on for new added ones
}

enum class MessageStatusType {
    SEEN,
    NOTSEEN
}