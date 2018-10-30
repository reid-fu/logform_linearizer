package linearizer.eng_heuristic;

public class ChildOrders {
	public static final String ORACLE = "oracle";
	public static final String ENG = "eng";
	public static final String[] ENG_CHILD_ORDER = {"Det","GenOwn","Arg0","Mod:1-2","Arg1","Arg2","Arg3","Arg4","Arg5"};
	public static final String[] ENG_BEFORE_PARENT = {"Det","GenOwn","Arg0","Mod:1-2"};
	public static final String[] ENG_AFTER_PARENT = {"Arg1","Arg2","Arg3","Arg4","Arg5"};
	
	public static String[] getChildOrder(String childOrderName) {
		if(childOrderName.equals(ENG)) {
			return ENG_CHILD_ORDER;
		}
		return null;
	}
}
