package engine.math

import org.hexworks.zircon.api.data.Size
import java.awt.Dimension

class Int2(var x: Int = 0, var y: Int = 0) {
    constructor(v: Int) : this(v, v)
    constructor(v: Dimension) : this(v.width, v.height)
    constructor(v: Int2) : this(v.x, v.y)

    operator fun plus(other: Int2) : Int2 {
        return Int2(x + other.x, y + other.y)
    }

    operator fun plusAssign(other: Int2) {
        x += other.x
        y += other.y
    }

    operator fun minus(other: Int2) : Int2 {
        return Int2(x - other.x, y - other.y)
    }

    operator fun minusAssign(other: Int2) {
        x -= other.x
        y -= other.y
    }

    operator fun times(other: Int2) : Int2 {
        return Int2(x * other.x, y * other.y)
    }

    operator fun timesAssign(other: Int2) {
        x *= other.x
        y *= other.y
    }

    operator fun div(other: Int2) : Int2 {
        return Int2(x / other.x, y / other.y)
    }

    operator fun divAssign(other: Int2) {
        x /= other.x
        y /= other.y
    }

    operator fun rem(other: Int2) : Int2 {
        return Int2(x % other.x, y % other.y)
    }

    operator fun remAssign(other: Int2) {
        x %= other.x
        y %= other.y
    }

    operator fun compareTo(other: Int2) : Int {
        return (x-other.x) + (y-other.y)
    }

    override fun equals(other: Any?) : Boolean {
        if (other !is Int2) return false
        return (other.x == x && other.y == y)
    }

    override fun hashCode(): Int {
        return 31 * x + y
    }

    override fun toString() : String {
        return "Int2($x, $y)"
    }

    fun toSize() : Size {
        return Size.create(x, y)
    }
}