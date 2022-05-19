package co.com.gulupagames.gamecore.model

import android.os.Parcel
import android.os.Parcelable

open class ParcelableGameData : Parcelable {
    private var level: Int
    private var levelLine: Int
    private var score: Int
    private var lives: Int
    private var bombs: Int
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(level)
        dest.writeInt(levelLine)
        dest.writeInt(score)
        dest.writeInt(lives)
        dest.writeInt(bombs)
    }

    constructor(level: Int, levelLine: Int, score: Int, lives: Int, bombs: Int) {
        this.level = level
        this.levelLine = levelLine
        this.score = score
        this.lives = lives
        this.bombs = bombs
    }

    protected constructor(`in`: Parcel) {
        level = `in`.readInt()
        levelLine = `in`.readInt()
        score = `in`.readInt()
        lives = `in`.readInt()
        bombs = `in`.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ParcelableGameData?> =
            object : Parcelable.Creator<ParcelableGameData?> {
                override fun createFromParcel(source: Parcel): ParcelableGameData? {
                    return ParcelableGameData(source)
                }

                override fun newArray(size: Int): Array<ParcelableGameData?> {
                    return arrayOfNulls(size)
                }
            }
    }
}