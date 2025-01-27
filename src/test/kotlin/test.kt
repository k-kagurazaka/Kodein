package com.github.salomonbrys.kodein.test

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import assertThrown
import com.github.salomonbrys.kodein.typeToken
import org.testng.annotations.Test
import kotlin.reflect.KClass
import kotlin.test.expect

public data class Person( public val name: String? = null )

public data class A(val b: B?)
public data class B(val c: C?)
public data class C(val a: A?)

Test(
        priority = 0,
        groups = arrayOf("binding")
)
public fun ProviderBindingGetInstance() {

    val kodein = Kodein { bind<Person>() with { Person() } }

    val p1: Person = kodein()
    val p2: Person = kodein()

    assert(System.identityHashCode(p1) != System.identityHashCode(p2))
}

Test(
        priority = 0,
        groups = arrayOf("binding")
)
public fun ProviderBindingGetProvider() {

    val kodein = Kodein { bind<Person>() with { Person() } }

    val p1 = kodein.provider<Person>()
    val p2 = kodein.provider<Person>()

    assert(System.identityHashCode(p1()) != System.identityHashCode(p2()))
}


Test(
        priority = 1,
        groups = arrayOf("binding")
)
public fun SingletonBindingGetInstance() {

    val kodein = Kodein { bind<Person>() with singleton { Person() } }

    val p1: Person = kodein()
    val p2: Person = kodein()

    assert(System.identityHashCode(p1) == System.identityHashCode(p2))
}

Test(
        priority = 1,
        groups = arrayOf("binding")
)
public fun SingletonBindingGetProvider() {

    val kodein = Kodein { bind<Person>() with singleton { Person() } }

    val p1 = kodein.provider<Person>()
    val p2 = kodein.provider<Person>()

    assert(System.identityHashCode(p1()) == System.identityHashCode(p2()))
}

Test(
        priority = 2,
        groups = arrayOf("binding")
)
public fun InstanceBindingGetInstance() {

    val p = Person()

    val kodein = Kodein { bind<Person>() with instance(p) }

    val p1: Person = kodein()
    val p2: Person = kodein()

    assert(System.identityHashCode(p1) == System.identityHashCode(p))
    assert(System.identityHashCode(p2) == System.identityHashCode(p))
}

Test(
        priority = 2,
        groups = arrayOf("binding")
)
public fun InstanceBindingGetProvider() {

    val p = Person()

    val kodein = Kodein { bind<Person>() with instance(p) }

    val p1 = kodein.provider<Person>()
    val p2 = kodein.provider<Person>()

    assert(System.identityHashCode(p1()) == System.identityHashCode(p))
    assert(System.identityHashCode(p2()) == System.identityHashCode(p))
}

Test(
        priority = 3,
        groups = arrayOf("binding")
)
public fun NullBindingGetInstance() {

    val kodein = Kodein {}

    val p = kodein.instanceOrNull<Person>()

    assert(p == null)
}

Test(
        priority = 3,
        groups = arrayOf("binding")
)
public fun NullBindingGetProvider() {

    val kodein = Kodein {}

    val p = kodein.providerOrNull<Person>()

    assert(p == null)
}


Test(
        priority = 10,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedProviderBindingGetInstance() {
    val kodein = Kodein {
        bind<Person>() with { Person() }
        bind<Person>("named") with { Person("Salomon") }
    }

    val p1: Person = kodein()
    val p2: Person = kodein("named")

    assert(p1.name == null)
    assert(p2.name == "Salomon")
}

Test(
        priority = 10,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedProviderBindingGetProvider() {
    val kodein = Kodein {
        bind<Person>() with { Person() }
        bind<Person>("named") with { Person("Salomon") }
    }

    val p1 = kodein.provider<Person>()
    val p2 = kodein.provider<Person>("named")

    assert(p1().name == null)
    assert(p2().name == "Salomon")
}

Test(
        priority = 11,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedSingletonBindingGetInstance() {
    val kodein = Kodein {
        bind<Person>() with singleton { Person() }
        bind<Person>("named") with singleton { Person("Salomon") }
    }

    val p1: Person = kodein("named")
    val p2: Person = kodein("named")

    assert(p1.name == "Salomon")
    assert(System.identityHashCode(p1) == System.identityHashCode(p2))
}

Test(
        priority = 11,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedSingletonBindingGetProvider() {
    val kodein = Kodein {
        bind<Person>() with singleton { Person() }
        bind<Person>("named") with singleton { Person("Salomon") }
    }

    val p1 = kodein.provider<Person>("named")
    val p2 = kodein.provider<Person>("named")

    assert(p1().name == "Salomon")
    assert(System.identityHashCode(p1()) == System.identityHashCode(p2()))
}

Test(
        priority = 12,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedInstanceBindingGetInstance() {

    val kodein = Kodein {
        bind<Person>() with instance(Person())
        bind<Person>("named") with instance(Person("Salomon"))
    }

    val p1: Person = kodein()
    val p2: Person = kodein("named")
    val p3: Person = kodein("named")

    assert(p1.name == null)
    assert(p2.name == "Salomon")
    assert(System.identityHashCode(p1) != System.identityHashCode(p2))
    assert(System.identityHashCode(p2) == System.identityHashCode(p3))
}

Test(
        priority = 12,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun NamedInstanceBindingGetProvider() {

    val kodein = Kodein {
        bind<Person>() with instance(Person())
        bind<Person>("named") with instance(Person("Salomon"))
    }

    val p1 = kodein.provider<Person>()
    val p2 = kodein.provider<Person>("named")
    val p3 = kodein.provider<Person>("named")

    assert(p1().name == null)
    assert(p2().name == "Salomon")
    assert(System.identityHashCode(p1()) != System.identityHashCode(p2()))
    assert(System.identityHashCode(p2()) == System.identityHashCode(p3()))
}

Test(
        priority = 13,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun ConstantBindingGetInstance() {

    val kodein = Kodein {
        constant("answer") with 42
    }

    val c: Int = kodein("answer")

    assert(c == 42)
}

Test(
        priority = 14,
        groups = arrayOf("named"),
        dependsOnGroups = arrayOf("binding")
)
public fun ConstantBindingGetProvider() {

    val kodein = Kodein {
        constant("answer") with 42
    }

    val c = kodein.provider<Int>("answer")

    assert(c() == 42)
}


Test(
        priority = 20,
        groups = arrayOf("loop"),
        dependsOnGroups = arrayOf("binding")
)
public fun DependencyLoop() {

    val kodein = Kodein {
        bind<A>() with singleton { A(it()) }
        bind<B>() with singleton { B(it()) }
        bind<C>() with singleton { C(it()) }
    }

    assertThrown<Kodein.DependencyLoopException> {
        kodein<A>()
    }
}

Test(
        priority = 21,
        groups = arrayOf("loop"),
        dependsOnGroups = arrayOf("binding", "named")
)
public fun NoDependencyLoop() {

    val kodein = Kodein {
        bind<A>() with singleton { A(it()) }
        bind<A>("root") with singleton { A(null) }
        bind<B>() with singleton { B(it()) }
        bind<C>() with singleton { C(it("root")) }
    }

    val a = kodein<A>()
    assert(a.b?.c?.a != null)
}

Test(
        priority = 30,
        groups = arrayOf("error"),
        dependsOnGroups = arrayOf("binding"),
        expectedExceptions = arrayOf()
)
public fun TypeNotFound() {

    val kodein = Kodein {}

    assertThrown<IllegalStateException> {
        kodein<Person>()
    }
}

Test(
        priority = 31,
        groups = arrayOf("error"),
        dependsOnGroups = arrayOf("binding", "named")
)
public fun NameNotFound() {

    val kodein = Kodein {
        bind<Person>() with { Person() }
        bind<Person>("named") with { Person("Salomon") }
    }

    assertThrown<IllegalStateException> {
        kodein<Person>("schtroumpf")
    }
}

Test(
        priority = 40,
        groups = arrayOf("erasure"),
        dependsOnGroups = arrayOf("binding")
)
public fun TypeErasure() {

    val la = listOf(A(null))
    val lb = listOf(B(null))

    val kodein = Kodein {
        bind<List<A>>() with instance( la )
        bind<List<B>>() with instance( lb )
    }
}
