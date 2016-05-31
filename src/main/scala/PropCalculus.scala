object PropCalculus {

  trait Theorem

  trait Atom extends Theorem
  class P extends Atom
  class Q extends Atom
  class R extends Atom
  class $[X <: Atom] extends Atom

  class &[X <: Theorem, Y <: Theorem] extends Theorem
  class ~[X <: Theorem] extends Theorem
  class |[X <: Theorem, Y <: Theorem] extends Theorem
  class ->[X <: Theorem, Y <: Theorem] extends Theorem

  // RULE OF JOINING: If x and y are theorems of the system, then so is the string <x ∧ y>.
  def join[X <: Theorem, Y <: Theorem](a: X, b: Y) = new (X & Y)

  // RULE OF SEPARATION: If <x ∧ y> is a theorem, then both x and y are theorems.
  def sepLeft[X <: Theorem, Y <: Theorem](ab: X & Y) = null.asInstanceOf[X]
  def sepRight[X <: Theorem, Y <: Theorem](ab: X & Y) = null.asInstanceOf[Y]

  // DOUBLE-TILDE RULE: The string '~~' can be deleted from any theorem.
  // It can also be inserted into any theorem, provided that the resulting string is itself well-formed.
  def doubleTil[X <: Theorem](a: X) = new ~[~[X]]
  def doubleTilRev[X <: Theorem](nna: ~[~[X]]) = null.asInstanceOf[X]

  // FANTASY RULE: If y can be derived when x is assumed to be a theorem, then <x ⊃ y> is a theorem.
  // CARRY-OVER RULE: Inside a fantasy, any theorem from the "reality" one level higher can be brought in and used
  // [automatically implemented with scoping].
  def withPremise[X <: Theorem, Y <: Theorem](f: X => Y) = new (X -> Y)

  // RULE OF DETACHMENT: If x and <x ⊃ y> are both theorems, then y is a theorem.
  def detach[X <: Theorem, Y <: Theorem](a: X, ab: X -> Y) = null.asInstanceOf[Y]

  // CONTRAPOSITIVE RULE: <x ⊃ y> and <~y ⊃ ~x> are interchangeable.
  def contrapos[X <: Theorem, Y <: Theorem](ab: X -> Y) = new (~[Y] -> ~[X])
  def contraposRev[X <: Theorem, Y <: Theorem](ab: ~[Y] -> ~[X]) = new (X -> Y)

  // DE MORGAN'S RULE: <~x ∧ ~y> and ~<x ∨ y> are interchangeable.
  def deMorgan[X <: Theorem, Y <: Theorem](ab: ~[X] & ~[Y]) = new ~[X | Y]
  def deMorganRev[X <: Theorem, Y <: Theorem](ab: ~[X | Y]) = new (~[X] & ~[Y])

  // SWITCHEROO RULE: <x ∨ y> and <~x ⊃ y> are interchangeable.
  def switcheroo[X <: Theorem, Y <: Theorem](ab: X | Y) = new (~[X] -> Y)
  def switcherooRev[X <: Theorem, Y <: Theorem](ab: ~[X] -> Y) = new (X | Y)
}
