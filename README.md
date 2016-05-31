# Scala Type-Level Number Theory

This repository contains the atoms, axioms and rules from [propositional calculus](https://en.wikipedia.org/wiki/Propositional_calculus) and [Typographical Number Theory](https://en.wikipedia.org/wiki/Typographical_Number_Theory) encoded in the Scala type system.

The project is heavily inspired by the formal systems described by Douglas Hofstadter in [Gödel, Escher, Bach: An Eternal Golden Braid](https://en.wikipedia.org/wiki/G%C3%B6del,_Escher,_Bach) and the descriptions and rule names are the same ones used from the book (despite not being the most common ones).

The mapping between proofs and programs follows roughly the [Curry–Howard correspondence](https://en.wikipedia.org/wiki/Curry%E2%80%93Howard_correspondence) in the sense that constructing an instance of a type is a necessary and sufficient condition for proving the theorem encoded by the type. As long as the instance is constructed using *only* the provided axiom variables and rule methods (i.e. without instantiating classes directly, using unsafe casts or reflection), a successful compilation acts effectively as an automated verification of the encoded proof.

## Examples

A trivial proof for P ⊢ ¬(¬P):

```scala
import PropCalculus._

val t1: P -> ~[~[P]] =
  withPremise { p: P =>
    doubleTil(p)
  }
```

A proof that every theorem is either true or false:

```scala
def t2[T <: Theorem]: (T | ~[T]) = {
  val a1: ~[T] -> ~[T]     = withPremise { p => p }
  switcherooRev(a1)
}
```

A more complex proof which makes use of the previous theorem:

```scala
val t3: ((P -> Q) & (~[P] -> Q)) -> Q =
  withPremise { premise: ((P -> Q) & (~[P] -> Q)) =>
    val a1: P -> Q           = sepLeft(premise)
    val a2: ~[Q] -> ~[P]     = contrapos(a1)
    val a3: ~[P] -> Q        = sepRight(premise)
    val a4: ~[Q] -> ~[~[P]]  = contrapos(a3)

    val a5: ~[Q] -> ~[P | ~[P]] =
      withPremise { premise2: ~[Q] =>
        val b1: ~[P]            = detach(premise2, a2)
        val b2: ~[~[P]]         = detach(premise2, a4)
        val b3: ~[P] & ~[~[P]]  = join(b1, b2)
        deMorgan(b3)
      }

    val a6: (P | ~[P]) -> Q  = contraposRev(a5)
    detach(t2[P], a6)
  }
```

A proof that 1 plus 1 equals 2:

```scala
import NumCalculus._

val t4: S[_0] + S[_0] == S[S[_0]] = {
  val a1: All[B, S[_0] + S[B] == S[S[_0] + B]]  = specification[S[_0]](axiom3)
  val a2: S[_0] + S[_0] == S[S[_0] + _0]        = specification[_0](a1)
  val a3: S[_0] + _0 == S[_0]                   = specification[S[_0]](axiom2)
  val a4: S[S[_0] + _0] == S[S[_0]]             = addS(a3)
  transitivity(a2, a4)
}
```

An invalid proof that 1 plus 1 equals 3:

```scala
  val t5: S[_0] + S[_0] == S[S[S[_0]]] = {
    val a1: All[B, S[_0] + S[B] == S[S[_0] + B]]  = specification[S[_0]](axiom3)
    val a2: S[_0] + S[_0] == S[S[_0] + _0]        = specification[_0](a1)
    val a3: S[_0] + _0 == S[S[_0]]                = specification[S[_0]](axiom2)
    val a4: S[S[_0] + _0] == S[S[S[_0]]]          = addS(a3)
    transitivity(a2, a4)
  }
  // -----
  // Compilation error (cleaned up, of course :))
  // -----
  // [error] type mismatch;
  // [error]  found   : ==[+[S[_0],_0],S[_0]]
  // [error]  required: ==[+[S[_0],_0],S[S[_0]]]
  // [error]     val a3: S[_0] + _0 == S[S[_0]]                = specification[S[_0]](axiom2)
  // [error]                                                                         ^
```
