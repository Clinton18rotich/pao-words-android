package com.example.paowords.data

data class WordEntry(
    val id: String, val word: String, val phonetic: String, val image: String, 
    val category: String, val emoji: String, val bio: String, val mnemonic: String
)

val WORD_DATA: List<WordEntry> = listOf(
    WordEntry("01", "Justice", "Just Ice", "A frozen judge's gavel", "Virtues", "⚖️🧊", "The quality of being fair and reasonable. In law, it is the administration of fairness.", "Imagine a judge's gavel made of solid ice. It slams down on a frozen scale. Smell the frost. Hear the crack of the ice. The word is Justice, but the image is Just Ice."),
    WordEntry("02", "Melancholy", "Melon Collie", "A sad watermelon and a Collie dog", "Emotions", "🍉🐶", "A feeling of pensive sadness, typically with no obvious cause.", "Imagine a giant, sad watermelon sitting next to a howling Collie dog. The dog is crying seeds. Taste the salty tears on the melon. Hear the dog's lonely howl. The word is Melancholy, but the image is a Melon Collie."),
    // ... (We will paste the remaining 98 entries here)
)
