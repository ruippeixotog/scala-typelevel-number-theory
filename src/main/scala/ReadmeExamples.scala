import PropCalculus._
import NumCalculus._

object ReadmeExamples {

  val t1: P -> ~[~[P]] =
    withPremise { p: P =>
      doubleTil(p)
    }

  def t2[T <: Theorem]: (T | ~[T]) = {
    val a1: ~[T] -> ~[T]     = withPremise { p => p }
    switcherooRev(a1)
  }

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

  val t4: S[_0] + S[_0] == S[S[_0]] = {
    val a1: All[B, S[_0] + S[B] == S[S[_0] + B]]  = specification[S[_0]](axiom3)
    val a2: S[_0] + S[_0] == S[S[_0] + _0]        = specification[_0](a1)
    val a3: S[_0] + _0 == S[_0]                   = specification[S[_0]](axiom2)
    val a4: S[S[_0] + _0] == S[S[_0]]             = addS(a3)
    transitivity(a2, a4)
  }

  //  val t5: S[_0] + S[_0] == S[S[S[_0]]] = {
  //    val a1: All[B, S[_0] + S[B] == S[S[_0] + B]]  = specification[S[_0]](axiom3)
  //    val a2: S[_0] + S[_0] == S[S[_0] + _0]        = specification[_0](a1)
  //    val a3: S[_0] + _0 == S[S[_0]]                = specification[S[_0]](axiom2)
  //    val a4: S[S[_0] + _0] == S[S[S[_0]]]          = addS(a3)
  //    transitivity(a2, a4)
  //  }
  //  // [error] type mismatch;
  //  // [error]  found   : ==[+[S[_0],_0],S[_0]]
  //  // [error]  required: ==[+[S[_0],_0],S[S[_0]]]
  //  // [error]     val a3: S[_0] + _0 == S[S[_0]]                = specification[S[_0]](axiom2)
  //  // [error]                                                                         ^
}
