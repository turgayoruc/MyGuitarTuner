package com.example.myguitartuner

import androidx.compose.runtime.MutableState

enum class MyEnumString() {
    STRING1, STRING2, STRING3, STRING4, STRING5, STRING6, STRINGDEFAULT;

    companion object {
        fun updateActiveString(item:MyNoteDataClass,string1: List<MyNoteDataClass>,string2: List<MyNoteDataClass>,string3: List<MyNoteDataClass>,string4: List<MyNoteDataClass>,string5: List<MyNoteDataClass>,string6: List<MyNoteDataClass> ) {

            when(item.nameEnum){
               MyEnumString.STRING1 ->{
                   string1.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item) {i.highlighting=false}
                   }
               }
                MyEnumString.STRING2 ->{
                   string2.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item){i.highlighting=false}
                   }
               }
                 MyEnumString.STRING3 ->{
                   string3.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item){i.highlighting=false}
                   }
               }
                 MyEnumString.STRING4 ->{
                   string4.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item){i.highlighting=false}
                   }
               }
                 MyEnumString.STRING5 ->{
                   string5.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item){i.highlighting=false}
                   }
               }
                 MyEnumString.STRING6 ->{
                   string6.forEach { i->
                       if (i==item){i.highlighting=true}
                       else if(i!=item){i.highlighting=false}
                   }
               }


                else -> {}
            }

        }
    }
}