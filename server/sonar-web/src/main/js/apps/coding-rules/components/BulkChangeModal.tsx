/*
 * SonarQube
 * Copyright (C) 2009-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import * as React from 'react';
import { Profile } from '../../../api/quality-profiles';
import Modal from '../../../components/controls/Modal';
import { translate } from '../../../helpers/l10n';
import { formatMeasure } from '../../../helpers/measures';

interface Props {
  action: string;
  onClose: () => void;
  referencedProfiles: { [profile: string]: Profile };
  profile?: Profile;
  total: number;
}

export default class BulkChangeModal extends React.PureComponent<Props> {
  handleCloseClick = (event: React.SyntheticEvent<HTMLButtonElement>) => {
    event.preventDefault();
    event.currentTarget.blur();
    this.props.onClose();
  };

  render() {
    const { action, profile, referencedProfiles, total } = this.props;
    const header =
      // prettier-ignore
      action === 'activate'
        ? `${translate('coding_rules.activate_in_quality_profile')} (${formatMeasure(total, 'INT')} ${translate('coding_rules._rules')})`
        : `${translate('coding_rules.deactivate_in_quality_profile')} (${formatMeasure(total, 'INT')} ${translate('coding_rules._rules')})`;

    return (
      <Modal contentLabel={header} onRequestClose={this.props.onClose}>
        <form>
          <header className="modal-head">
            <h2>{header}</h2>
          </header>

          <div className="modal-body">
            <div className="modal-field">
              <h3>
                <label htmlFor="coding-rules-bulk-change-profile">
                  {action === 'activate'
                    ? translate('coding_rules.activate_in')
                    : translate('coding_rules.deactivate_in')}
                </label>
              </h3>
              {profile ? (
                <h3 className="readonly-field">
                  {profile.name}
                  {action !== 'change-severity' && `— ${translate('are_you_sure')}`}
                </h3>
              ) : (
                <select id="coding-rules-bulk-change-profile" multiple={true}>
                  {Object.values(referencedProfiles).map(profile => (
                    <option
                      key={profile.key}
                      value={profile.key}
                      selected={Object.keys(referencedProfiles).length === 1}>
                      {profile.name}
                      {' - '}
                      {profile.language}
                    </option>
                  ))}
                </select>
              )}
            </div>
          </div>

          <footer className="modal-foot">
            <button id="coding-rules-submit-bulk-change" type="submit">
              {translate('apply')}
            </button>
            <button className="button-link" onClick={this.handleCloseClick} type="reset">
              {translate('close')}
            </button>
          </footer>
        </form>
      </Modal>
    );
  }
}
