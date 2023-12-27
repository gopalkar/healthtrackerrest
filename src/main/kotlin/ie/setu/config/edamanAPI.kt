package ie.setu.config

class edamanAPI {
    companion object {
        fun getAPIUrl(): String {

            val APP_ID = "383486ed"
            val APP_KEY = "8547a061517dd6b776f70d7ef39a4268"

            val apiUrl: String = "https://api.edamam.com/api/food-database/v2/parser?app_id=${APP_ID}&app_key=${APP_KEY}"

            return apiUrl
        }
    }
}