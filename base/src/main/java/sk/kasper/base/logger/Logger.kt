package sk.kasper.base.logger

object Logger {

    var loge: (tag: String, msg: String, e: Exception?) -> Unit = { tag, msg, e -> Unit }

}