package views;

import org.junit.Before;
import org.junit.Test;
import repositories.AbstractRepositoryTest;
import repositories.TagRepository;

/**
 * Created by revy on 07.04.16.
 */
public class TagViewTest extends AbstractRepositoryTest {
    private TagRepository tagRepository;

    @Before
    public void setUp() {
        tagRepository = new TagRepository();
    }

}
