package com.example.paowords.data

data class PAOEntry(
    val number: String,
    val person: String,
    val action: String,
    val objectName: String,
    val type: String,       // "Real Person" or "Fictional"
    val category: String,   // e.g. "Norse God & Marvel Superhero"
    val bio: String,        // 2-3 sentence Wikipedia-style summary
    val source: String,     // e.g. "Thor (2011), Marvel Cinematic Universe"
    val mnemonic: String    // How to remember this number
)
