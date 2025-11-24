package me.dennise.fitnest

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROFILE_DETAILS = "profile_detail"
    const val PROFILE_EDIT = "profile_edit"
    const val WORKOUT_ADD = "workout_add"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"

    fun workoutDetail(workoutId: Int) = "workout_detail/$workoutId"
}