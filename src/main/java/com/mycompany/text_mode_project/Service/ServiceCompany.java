package com.mycompany.text_mode_project.Service;

import com.mycompany.text_mode_project.model.Company;
import java.util.List;

public class ServiceCompany {

    private List<Company> companies;

    public ServiceCompany(List<Company> companies) {
        this.companies = companies;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public void deleteCompany(Company company) {
        companies.remove(company);
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public String getAllCompaniesString() {
        if (companies.isEmpty()) {
            return "No companies available.";
        }
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Company c : companies) {
            sb.append(index++).append(". ").append(c.toString()).append("\n");
        }
        return sb.toString();
    }
}
