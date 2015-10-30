/**
 *    Copyright 2015 Fondazione Bruno Kessler
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
