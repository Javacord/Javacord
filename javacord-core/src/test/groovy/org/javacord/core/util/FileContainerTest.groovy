package org.javacord.core.util

import spock.lang.Specification
import spock.lang.Subject

import java.awt.image.BufferedImage

@Subject(FileContainer)
class FileContainerTest extends Specification {

    def 'converting FileContainer with BufferedImage to byte array returns some bytes'() {
        given:
            def image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
            def fileContainer = new FileContainer(image, 'file.png')

        expect:
            fileContainer.asByteArray(null).join()
    }

}
