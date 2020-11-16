package com.homeworkshop.notemvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class), version = 3)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        //operator fun invoke jest funkcją wywoływaną w chwili gdy utworzymy obiekt tej klasy np NoteDatabase()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "NoteDB.db"
                ).build()

    }

//    companion object {
//        private var instance: NoteDatabase? = null
//        @JvmStatic
//        @Synchronized
//        fun getInstance(context: Context): NoteDatabase? {
//            if (instance == null) {
//                instance = Room.databaseBuilder(context.applicationContext,
//                        NoteDatabase::class.java, "note_database")
//                        .fallbackToDestructiveMigration()
////                        .addCallback(roomCallback) // wywołujemy obiekt roomCallback który wypełnia jednorazowa bazę danych przy tworzeniu jej
//                        .build()
//            }
//            return instance
//        }
////        //Wypełniamy nowo utworzoną bazę danych danymi, żeby nie była pusta na starcie.
////        private val roomCallback: Callback = object : Callback() {
////            //Ctrl+ o pokazuje wszystkie metody do nadpisania
////            override fun onCreate(db: SupportSQLiteDatabase) {
////                super.onCreate(db)
////                PopulateDbAsyncTask(instance).execute()
////            }
////        }
//
//    }

    //Klasa która asynchronichnie wypełnia bazę danych
//    private class PopulateDbAsyncTask(noteDatabase: NoteDatabase?) : AsyncTask<Void?, Void?, Void?>() {
//        private val noteDao: NoteDao
//        protected override fun doInBackground(vararg params: Void?): Void? {
//            noteDao.insert(Note("Title 1", "Description 1", 1))
//            noteDao.insert(Note("Title 2", "Description 2", 2))
//            noteDao.insert(Note("Title 3", "Description 3", 3))
//            noteDao.insert(Note("Title 4", "Description 4", 4))
//            return null
//        }
//
//        init {
//            noteDao = noteDatabase!!.noteDao()
//        }
//    }
}
