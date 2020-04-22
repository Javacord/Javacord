package org.javacord.core.util

import org.javacord.core.DiscordApiImpl
import org.javacord.core.util.concurrent.ThreadPoolImpl
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.awt.image.BufferedImage

@Subject(FileContainer)
class FileContainerTest extends Specification {

    @Shared
    @AutoCleanup("shutdown")
    def threadPool = new ThreadPoolImpl()

    def 'converting FileContainer with BufferedImage to byte array returns some bytes'() {
        given:
            def image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
            def fileContainer = new FileContainer(image, 'file.png')
            DiscordApiImpl api = Mock {
                getThreadPool() >> threadPool
            }

        expect:
            fileContainer.asByteArray(api).join()
    }

    def 'creating FileContainer with BufferedImage and unsupported file format throws exception'() {
        when:
            new FileContainer(Stub(BufferedImage), 'file.txt')

        then:
            IllegalArgumentException iae = thrown()
            iae.message == 'No image writer found for format "txt"'
    }

    def 'configuring FileContainer with BufferedImage with unsupported file format throws exception'() {
        given:
            def fileContainer = new FileContainer(Stub(BufferedImage), 'file.png')

        when:
            fileContainer.setFileTypeOrName 'file.txt'

        then:
            IllegalArgumentException iae = thrown()
            iae.message == 'No image writer found for format "txt"'
    }

}
