package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.stock.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department,String > , JpaSpecificationExecutor <Department> {
    
}
