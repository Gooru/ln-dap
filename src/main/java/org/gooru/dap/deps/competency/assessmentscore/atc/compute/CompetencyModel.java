package org.gooru.dap.deps.competency.assessmentscore.atc.compute;

/**
 * @author mukul@gooru
 */
public class CompetencyModel {

	private String domainCode;;
    private String competencyCode;
    private Integer status;
    private Integer competencySeq;
    
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getCompetencyCode() {
		return competencyCode;
	}
	public void setCompetencyCode(String competencyCode) {
		this.competencyCode = competencyCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCompetencySeq() {
		return competencySeq;
	}
	public void setCompetencySeq(Integer competencySeq) {
		this.competencySeq = competencySeq;
	}
    

    
}
