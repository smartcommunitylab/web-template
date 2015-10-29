package it.smartcommunitylab.webtemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

		Set attrs = a.getAttributes().keySet();

		for (final Object attr : attrs) {
			final Object value = a.getPrincipal().getAttributes().get(attr);

			if (value == null) {
				continue;
			}

			if (value instanceof List) {
				final List list = (List) value;

				for (final Object o : list) {
					grantedAuthorities.add(new SimpleGrantedAuthority(o
							.toString()));
				}

			} else {
				grantedAuthorities.add(new SimpleGrantedAuthority(value
						.toString()));
			}

		}

		return new User(a.getPrincipal().getName(), "no_password", true, true,
				true, true, grantedAuthorities);

	}

}
