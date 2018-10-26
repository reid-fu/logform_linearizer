package linearizer;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import util.WordFeatUtil;

@RunWith(MockitoJUnitRunner.class)
public class TestLinearizer {
	@Mock
	private LogicalForm lf;
	private Linearizer uut = new DFSLinearizer();
	
	@Test
	public void testRelSeq() {
		String[] orderArr = {"(","w0","w1","w2",")"};
		List<String> order = new ArrayList<>(Arrays.asList(orderArr));
		WordFeatures featsw0 = new WordFeatures();
		WordFeatUtil.addFeatures(featsw0,"id","w0","PR","Arg0");
		WordFeatures featsw1 = new WordFeatures();
		WordFeatUtil.addFeatures(featsw1,"id","w1");
		WordFeatures featsw2 = new WordFeatures();
		WordFeatUtil.addFeatures(featsw2,"id","w0","PR","Arg1");
		
		Mockito.when(lf.getWordFeatures("w0")).thenReturn(featsw0);
		Mockito.when(lf.getWordFeatures("w1")).thenReturn(featsw1);
		Mockito.when(lf.getWordFeatures("w2")).thenReturn(featsw2);
		uut.putRelationInSequence(lf, order);
		
		String[] expOrderArr = {"(","Arg0","w0","w1","Arg1","w2",")"};
		List<String> expOrder = Arrays.asList(expOrderArr);
		assertEquals(expOrder, order);
	}
}
