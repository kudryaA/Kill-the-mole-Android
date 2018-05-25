package kudrya.killthemole.core

class CreatorRequest(url: String, parameters: Map<String, String> = HashMap()) {
    private val domain = "http://172.16.60.104"
    private val port = 1488
    val result: String
    init {
        val builder = StringBuilder()
        builder.append(domain).append(":").append(port).append("/").append(url)
        if (parameters.isNotEmpty()) {
            builder.append("?")
            var k = 0
            parameters.entries.forEach { item ->
                val key = item.key
                val value = item.value
                if (k != 0)
                    builder.append("&")
                builder.append("$key=$value")
                k++
            }
        }
        result = builder.toString()
    }
}