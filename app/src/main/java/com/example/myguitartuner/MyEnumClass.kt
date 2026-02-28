package com.example.myguitartuner

import androidx.compose.runtime.MutableState

enum class MyEnumString() {
    SITRING1, SITRING2, SITRING3, SITRING4, SITRING5, SITRING6;

    companion object {
        fun updateIsSelected(enumString: MutableState<MyEnumString>,item:MyNoteDataClass,string1: List<MyNoteDataClass>,string2: List<MyNoteDataClass>,string3: List<MyNoteDataClass>,string4: List<MyNoteDataClass>,string5: List<MyNoteDataClass>,string6: List<MyNoteDataClass> ) {
           // var whichString: List<MyNoteDataClass>
            when(enumString.value){
               MyEnumString.SITRING1 ->{
                   string1.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item) {i.isSelected=false}
                   }
               }
                MyEnumString.SITRING2 ->{
                   string2.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item){i.isSelected=false}
                   }
               }
                 MyEnumString.SITRING3 ->{
                   string3.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item){i.isSelected=false}
                   }
               }
                 MyEnumString.SITRING4 ->{
                   string4.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item){i.isSelected=false}
                   }
               }
                 MyEnumString.SITRING5 ->{
                   string5.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item){i.isSelected=false}
                   }
               }
                 MyEnumString.SITRING6 ->{
                   string6.forEach { i->
                       if (i==item){i.isSelected=true}
                       else if(i!=item){i.isSelected=false}
                   }
               }


                else -> {}
            }

        }
    }
}