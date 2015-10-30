package it.smartcommunitylab.webtemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CasUserDetailWrapper implements
		AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

	@SuppressWarnings("rawtypes")
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token)
			throws UsernameNotFoundException {
		Assertion a = token.getAssertion();

		final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		Collection attrs = a.getPrincipal().getAttributes().values();

		for (final Object attr : attrs) {

			if (attr == null) {
				continue;
			}

			if (attr instanceof List) {
				final List list = (List) attr;

				for (final Object o : list) {
					grantedAuthorities.add(new SimpleGrantedAuthority(o
							.toString()));
				}

			} else {
				if (attr.toString().trim().length() > 0) {
					grantedAuthorities.add(new SimpleGrantedAuthority(attr
							.toString()));
				}
			}

		}

		return new User(a.getPrincipal().getName(), "no_password", true, true,
				true, true, grantedAuthorities);

	}

}
