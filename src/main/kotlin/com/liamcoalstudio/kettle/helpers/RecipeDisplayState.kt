package com.liamcoalstudio.kettle.helpers

data class RecipeDisplayState(
    val craftingMenuBookOpen: Boolean,
    val craftingMenuBookFiltering: Boolean,
    val furnaceBookOpen: Boolean,
    val furnaceBookFiltering: Boolean,
    val blastFurnaceBookOpen: Boolean,
    val blastFurnaceBookFiltering: Boolean,
    val smokerBookOpen: Boolean,
    val smokerBookFiltering: Boolean,
)
