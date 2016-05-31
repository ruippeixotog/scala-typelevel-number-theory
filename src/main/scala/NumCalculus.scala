import PropCalculus._

object NumCalculus extends VarManipulation {

  sealed trait Term

  sealed trait Var extends Term
  class A extends Var
  class B extends Var
  class C extends Var
  class D extends Var
  class E extends Var
  class $[X <: Var] extends Var

  class _0 extends Term
  class S[X <: Term] extends Term
  class +[X <: Term, Y <: Term] extends Term
  class *[X <: Term, Y <: Term] extends Term

  class ==[X <: Term, Y <: Term] extends Theorem

  class Exists[V <: Var, X <: Theorem] extends Theorem
  class All[V <: Var, X <: Theorem] extends Theorem

  val axiom1 = new All[A, ~[S[A] == _0]]
  val axiom2 = new All[A, (A + _0) == A]
  val axiom3 = new All[A, All[B, (A + S[B]) == S[A + B]]]
  val axiom4 = new All[A, (A * _0) == _0]
  val axiom5 = new All[A, All[B, (A * S[B]) == ((A * B) + A)]]

  // RULE OF SPECIFICATION: Suppose u is a variable which occurs inside the string x. If the string Vu: x is a theorem,
  // then so is x, and so are any strings made from x by replacing u, wherever it occurs, by one and the same term.
  // (Restriction: The term which replaces u must not contain any variable that is quantified in x.)
  // TODO: restriction is not enforced yet
  def specification[X <: Term] = new {
    def apply[V <: Var, Y <: Theorem](t: All[V, Y])(implicit ev: Replace[V, X, Y]) = null.asInstanceOf[ev.Out]
  }

  // RULE OF GENERALIZATION: Suppose x is a theorem in which u, a var occurs free. Then Vu: x is a theorem.
  // (Restriction: No generalization is allowed in a fantasy on any var which appeared free in the fantasy's premise.)
  // TODO: not implemented

  // RULE OF INTERCHANGE: Suppose u is a variable. Then the strings Vu:~x and ~Eu:x are interchangeable anywhere inside any
  // theorem.
  def interchange[V <: Var, X <: Theorem](t: All[V, ~[X]]) = new ~[Exists[V, X]]
  def interchangeRev[V <: Var, X <: Theorem](t: ~[Exists[V, X]]) = new All[V, ~[X]]

  // RULE OF EXISTENCE: Suppose a term (which may contain variables as as they are free) appears once. or multiply, in a
  // theorem. Then an several, or all) of the appearances of the term may be replaced variable which otherwise does not
  // occur in the theorem, and corresponding existential quantifier must be placed in front.
  // TODO: not implemented

  // RULES OF EQUALITY:
  // SYMMETRY: If r = s is a theorem, then so is s = r.
  // TRANSITIVITY: If r = s and s = t are theorems, then so is r = t.
  def symmetry[X <: Term, Y <: Term](t: X == Y) = new (Y == X)
  def transitivity[X <: Term, Y <: Term, Z <: Term](t1: X == Y, t2: Y == Z) = new (X == Z)

  // RULES OF SUCCESSORSHIP:
  // ADD S: If r = t is atheorem, then Sr = St is a theorem.
  // DROP S: If Sr = St is a theorem, then r = t is a theorem.
  def addS[X <: Term, Y <: Term](t: X == Y) = new (S[X] == S[Y])
  def dropS[X <: Term, Y <: Term](t: S[X] == S[Y]) = new (X == Y)
}

trait VarManipulation {
  import NumCalculus._

  trait =:!=[A, B]
  implicit def neq[A, B] : A =:!= B = new =:!=[A, B] {}
  implicit def neqAmbig1[A] : A =:!= A = ???
  implicit def neqAmbig2[A] : A =:!= A = ???

  class ReplaceTerm[V <: Var, X <: Term, Y <: Term] { type Out <: Term }

  implicit def replaceInZero[V <: Var, X <: Term] = new ReplaceTerm[V, X, _0] { type Out = _0 }

  implicit def replaceInVar1[V <: Var, X <: Term] = new ReplaceTerm[V, X, V] { type Out = X }
  implicit def replaceInVar2[V <: Var, X <: Term, Y <: Var](implicit ev: V =:!= Y) =
    new ReplaceTerm[V, X, Y] { type Out = Y }

  implicit def replaceInSucc[V <: Var, X <: Term, M <: Term](implicit rm: ReplaceTerm[V, X, M]) =
    new ReplaceTerm[V, X, S[M]] { type Out = S[rm.Out] }

  implicit def replaceInSum[V <: Var, X <: Term, M <: Term, N <: Term](
      implicit rm: ReplaceTerm[V, X, M], rn: ReplaceTerm[V, X, N]) =
    new ReplaceTerm[V, X, M + N] { type Out = rm.Out + rn.Out }

  implicit def replaceInProd[V <: Var, X <: Term, M <: Term, N <: Term](
      implicit rm: ReplaceTerm[V, X, M], rn: ReplaceTerm[V, X, N]) =
    new ReplaceTerm[V, X, M * N] { type Out = rm.Out * rn.Out }

  class Replace[V <: Var, X <: Term, Y <: Theorem] { type Out <: Theorem }

  implicit def replaceInTil[V <: Var, X <: Term, M <: Theorem](implicit rm: Replace[V, X, M]) =
    new Replace[V, X, ~[M]] { type Out = ~[rm.Out] }

  implicit def replaceInEquals[V <: Var, X <: Term, M <: Term, N <: Term](
      implicit rm: ReplaceTerm[V, X, M], rn: ReplaceTerm[V, X, N]) =
    new Replace[V, X, M == N] { type Out = rm.Out == rn.Out }

  implicit def replaceInExists[V <: Var, X <: Term, V2 <: Var, M <: Theorem](implicit rm: Replace[V, X, M]) =
    new Replace[V, X, Exists[V2, M]] { type Out = Exists[V2, rm.Out] }

  implicit def replaceInAll[V <: Var, X <: Term, V2 <: Var, M <: Theorem](implicit rm: Replace[V, X, M]) =
    new Replace[V, X, All[V2, M]] { type Out = All[V2, rm.Out] }
}
