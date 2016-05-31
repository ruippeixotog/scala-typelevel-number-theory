import PropCalculus._
import NumCalculus._

object NumCalculusExamples extends App {

  // page 217
  val t1: ~[S[_0] == _0] = specification[_0](axiom1)

  // page 218
  val t2: ~[Exists[A, S[A] == _0]] = interchange(axiom1)

  // page 219
  val t3: S[_0] + S[_0] == S[S[_0]] = {
    val a1: All[B, S[_0] + S[B] == S[S[_0] + B]]  = specification[S[_0]](axiom3)
    val a2: S[_0] + S[_0] == S[S[_0] + _0]        = specification[_0](a1)
    val a3: S[_0] + _0 == S[_0]                   = specification[S[_0]](axiom2)
    val a4: S[S[_0] + _0] == S[S[_0]]             = addS(a3)
    transitivity(a2, a4)
  }

  val t4: S[_0] * S[_0] == S[_0] = {
    val a1: All[B, S[_0] * S[B] == ((S[_0] * B) + S[_0])]         = specification[S[_0]](axiom5)
    val a2: S[_0] * S[_0] == ((S[_0] * _0) + S[_0])               = specification[_0](a1)
    val a3: All[B, ((S[_0] * _0) + S[B]) == S[(S[_0] * _0) + B]]  = specification[S[_0] * _0](axiom3)
    val a4: ((S[_0] * _0) + S[_0]) == S[(S[_0] * _0) + _0]        = specification[_0](a3)
    val a5: ((S[_0] * _0) + _0) == (S[_0] * _0)                   = specification[S[_0] * _0](axiom2)
    val a6: (S[_0] * _0) == _0                                    = specification[S[_0]](axiom4)
    val a7: ((S[_0] * _0) + _0) == _0                             = transitivity(a5, a6)
    val a8: S[(S[_0] * _0) + _0] == S[_0]                         = addS(a7)
    val a9: ((S[_0] * _0) + S[_0]) == S[_0]                       = transitivity(a4, a8)
    transitivity(a2, a9)
  }
}
