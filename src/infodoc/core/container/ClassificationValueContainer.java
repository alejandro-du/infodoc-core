package infodoc.core.container;

import infodoc.core.dto.ClassificationValue;

@SuppressWarnings("unchecked")
public class ClassificationValueContainer extends UserGroupFilteredContainer<ClassificationValue> {
	
	private static final long serialVersionUID = 1L;

	public ClassificationValueContainer() {
		super(ClassificationValue.class, "classification.userGroup.id");
	}

	public ClassificationValue getByNameAndClassificationId(String name, Long classificationId) {
		return singleQuery(
			"select cv" +
			" from ClassificationValue cv" +
			" where cv.name = ?" +
			" and cv.classification.id = ?",
			new Object[] {name, classificationId});
	}

}
