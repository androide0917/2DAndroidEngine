package co.com.gulupagames.gamecore.model

class Rectangle(val posX: Int, val posY: Int, val width: Int, val height: Int) {

    companion object {
        fun checkCollision(r: Rectangle, r2: Rectangle): Boolean {
            return r.posX + r.width >= r2.posX && r.posY + r.height >= r2.posY && r.posX <= r2.posX + r2.width && r.posY <= r2.posY + r2.height
        }
    }

}