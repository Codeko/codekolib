package com.codeko.util.estructuras;

import com.codeko.util.Str;

/**
 * Objeto genérico para almacenar un par de objetos. Pensado para casos en los que haya que tratar grupos de dos valores pero sin el coste de un diccionario
 * y sin la poca claridad de un array (ya que ambos objetos se presuponen de clases distintas).
 * @author codeko
 */
public class Par<A, B> {

    A a = null;
    B b = null;

    public Par(A a, B b) {
        setA(a);
        setB(b);
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "A:" + Str.noNulo(getA()) + " B:" + Str.noNulo(getB());
    }
}
