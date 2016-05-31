import PropCalculus._

object PropCalculusExamples extends App {

  // page 184
  val t1: P -> ~[~[P]] =
    withPremise { p: P => doubleTil(p) }

  val t2: (P & Q) -> (Q & P) =
    withPremise { pq: (P & Q) =>
      val p: P = sepLeft(pq)
      val q: Q = sepRight(pq)
      join(q, p)
    }

  // page 185
  val t3: P -> (Q -> (P & Q)) =
    withPremise { p: P =>
      withPremise { q: Q => join(p, q) }
    }

  // page 189
  val t4: ((P -> Q) & (~[P] -> Q)) -> Q =
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
      val a7: ~[P] -> ~[P]     = withPremise(identity)
      val a8: P | ~[P]         = switcherooRev(a7)
      detach(a8, a6)
    }

  // page 196 (generalized)
  def t5[X <: Theorem]: (P & ~[P]) -> X =
    withPremise { premise: (P & ~[P]) =>
      val a1: P           = sepLeft(premise)
      val a2: ~[P]        = sepRight(premise)

      val a3: ~[X] -> ~[~[P]] =
        withPremise { premise2: ~[X] => doubleTil(a1) }

      val a4: ~[P] -> X   = contraposRev(a3)
      detach(a2, a4)
    }
}
