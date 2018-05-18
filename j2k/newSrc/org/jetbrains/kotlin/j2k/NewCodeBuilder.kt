/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.j2k

import org.jetbrains.kotlin.j2k.tree.JKClass
import org.jetbrains.kotlin.j2k.tree.JKTreeElement
import org.jetbrains.kotlin.j2k.tree.JKUniverseClass
import org.jetbrains.kotlin.j2k.tree.visitors.JKVisitor
import org.jetbrains.kotlin.utils.Printer

class NewCodeBuilder {

    val builder = StringBuilder()
    val printer = Printer(builder)

    private fun classKindString(kind: JKClass.ClassKind): String = when (kind) {
        JKClass.ClassKind.ABSTRACT -> "abstract class"
        JKClass.ClassKind.ANNOTATION -> "annotation class"
        JKClass.ClassKind.CLASS -> "class"
        JKClass.ClassKind.ENUM -> "enum class"
        JKClass.ClassKind.INTERFACE -> "interface"
    }


    inner class Visitor : JKVisitor<Unit, Unit> {
        override fun visitElement(element: JKTreeElement, data: Unit) {
            printer.print("/* !!! Hit visitElement for element type: ${element::class} !!! */")
        }

        override fun visitUniverseClass(universeClass: JKUniverseClass, data: Unit) {
            printer.print(classKindString(universeClass.classKind))
            builder.append(" ")
            printer.print(universeClass.name.value)
            if (universeClass.declarationList.isNotEmpty()) {
                printer.println("{")
                printer.pushIndent()
                universeClass.declarationList.forEach { it.accept(this, data) }
                printer.popIndent()
                printer.println("}")
            }
        }
    }


    fun printCodeOut(root: JKTreeElement): String {
        Visitor().also { root.accept(it, Unit) }
        return builder.toString()
    }
}