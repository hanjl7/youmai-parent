package com.youmai.service;

import com.youmai.pojo.TbSeller;
import com.youmai.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserDetailsServiceImpl
 * @Description: 认证类
 * @Author: 泊松
 * @Date: 2018/9/15 20:51
 * @Version: 1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthor = new ArrayList<>();
        grantedAuthor.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //得到对象
        TbSeller tbSeller = sellerService.findOne(username);
        if (tbSeller != null && tbSeller.getStatus().equals("1")) {
            return new User(username, tbSeller.getPassword(), grantedAuthor);
        } else {
            return null;
        }
    }
}
