package me.dennise.fitnest.data


object Session {
    private var currentUser: User? = null

    fun loginUser(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun getCurrentUser(): User? = currentUser

    fun getCurrentUserId(): Int? = currentUser?.id

    fun isLoggedIn(): Boolean = currentUser != null
}

