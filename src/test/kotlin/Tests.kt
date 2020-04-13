import org.junit.AfterClass
import org.junit.Assert
import org.junit.Test
import ru.vldf.fed.Api
import java.io.File

internal class Tests {
    private var userId = 1
    private lateinit var message: String
    private lateinit var api: Api

    private val server = "130.61.203.95"

    companion object {
        val userName = System.currentTimeMillis().toString()
        @AfterClass
        @JvmStatic fun clean() {
            val sep = File.separator
            File("users${sep}$userName").deleteOnExit()
        }
    }

    @Test
    fun testAll() {
        register()
        messageSendAndGetLast()
    }

    private fun register() {
        api = Api(userName, serverAddress = server)
    }

    private fun messageSendAndGetLast() {
        val time = System.currentTimeMillis() - 10*1000 // 10*1000 = 10 sec. Inaccuracy due to time deviation

        val rnd = System.currentTimeMillis().toString()
        message = "test$rnd"
        val respSend = api.messageSend(userId, message)

        Assert.assertTrue(respSend.has("status") && respSend["status"].asString == "ok")

        val lastMessages = api.getLastMessages(userId, time)

        Assert.assertTrue(lastMessages.isNotEmpty())
        Assert.assertEquals(message, lastMessages.last().message)
    }
}