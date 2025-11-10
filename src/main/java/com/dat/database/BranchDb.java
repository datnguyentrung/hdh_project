package com.dat.database;

import com.dat.domain.Branch;
import java.util.ArrayList;
import java.util.List;

public class BranchDb {
    private static BranchDb instance;
    private List<Branch> branches;

    private BranchDb() {
        initializeData();
    }

    public static BranchDb getInstance() {
        if (instance == null) {
            instance = new BranchDb();
        }
        return instance;
    }

    private void initializeData() {
        branches = new ArrayList<>();

        Branch branch1 = new Branch(1, true);
        Branch branch2 = new Branch(2, true);
        Branch branch3 = new Branch(3, true);

        branches.add(branch1);
        branches.add(branch2);
        branches.add(branch3);
    }

    public List<Branch> getAllBranches() {
        return new ArrayList<>(branches);
    }

    public Branch getBranchById(Integer id) {
        return branches.stream()
                .filter(branch -> branch.getIdBranch().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public boolean updateBranch(Branch updatedBranch) {
        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getIdBranch().equals(updatedBranch.getIdBranch())) {
                branches.set(i, updatedBranch);
                return true;
            }
        }
        return false;
    }

    public boolean deleteBranch(Integer id) {
        return branches.removeIf(branch -> branch.getIdBranch().equals(id));
    }
}
