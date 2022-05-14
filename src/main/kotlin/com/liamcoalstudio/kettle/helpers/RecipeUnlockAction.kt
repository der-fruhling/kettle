package com.liamcoalstudio.kettle.helpers

class RecipeUnlockAction private constructor(
    val type: Type,
    val a1: Array<Identifier>,
    val a2: Array<Identifier>,
    val displayState: RecipeDisplayState
) {
    enum class Type(val action: Int) {
        Init(0),
        Add(1),
        Remove(2),
    }

    companion object {
        val DEFAULT_DISPLAY = RecipeDisplayState(
            craftingMenuBookOpen = false,
            craftingMenuBookFiltering = false,
            furnaceBookOpen = false,
            furnaceBookFiltering = false,
            blastFurnaceBookOpen = false,
            blastFurnaceBookFiltering = false,
            smokerBookOpen = false,
            smokerBookFiltering = false
        )

        val EMPTY_INIT = init(arrayOf(), arrayOf())
        val EMPTY_ADD = add(arrayOf())
        val EMPTY_REMOVE = remove(arrayOf())

        fun init(
            array1: Array<Identifier>,
            array2: Array<Identifier>,
            displayState: RecipeDisplayState = DEFAULT_DISPLAY
        ) = RecipeUnlockAction(Type.Init, array1, array2, displayState)

        fun add(array: Array<Identifier>, displayState: RecipeDisplayState = DEFAULT_DISPLAY) =
            RecipeUnlockAction(Type.Add, array, arrayOf(), displayState)

        fun remove(array: Array<Identifier>, displayState: RecipeDisplayState = DEFAULT_DISPLAY) =
            RecipeUnlockAction(Type.Remove, array, arrayOf(), displayState)
    }
}