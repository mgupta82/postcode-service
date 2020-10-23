package com.auspost.postcode.repository;

import com.auspost.postcode.repository.entity.Postcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostcodeRepository extends JpaRepository<Postcode,Integer> {

}
