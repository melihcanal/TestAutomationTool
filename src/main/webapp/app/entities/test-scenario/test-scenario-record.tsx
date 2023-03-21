import React, { useEffect } from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import Button from '@mui/material/Button';
import DeleteIcon from '@mui/icons-material/Delete';
import SendIcon from '@mui/icons-material/Send';
import Stack from '@mui/material/Stack';
import { startWebDriver } from './test-scenario.reducer';
import { useAppDispatch } from 'app/config/store';

export const TestScenarioRecord = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    console.log('record useEffect');
    dispatch(startWebDriver({}));
  }, []);

  const list = [
    'Navigate to the https://google.com',
    'Type "something" in //div[@id="input"]',
    'Click to element //div[@id="search"]',
    'Click to element (//li[@id="list"])[1]',
    'Type "something" in //div[@id="input"]',
    'Click to element //div[@id="search"]',
    'Click to element (//li[@id="list"])[1]',
  ];

  return (
    <div>
      <Stack direction="row" alignItems="center" spacing={2}>
        <Button variant="contained" component="label">
          Start Recording
          <input hidden accept="image/*" multiple type="file" />
        </Button>
      </Stack>
      <List
        sx={{
          width: '100%',
          maxWidth: 360,
          bgcolor: 'background.paper',
          position: 'relative',
          overflow: 'auto',
          maxHeight: 700,
          '& ul': { padding: 0 },
        }}
        subheader={<li />}
      >
        {[1].map(sectionId => (
          <li key={`section-${sectionId}`}>
            <ul>
              <ListSubheader>{`Sample test scenario`}</ListSubheader>
              {list.map((id, item) => (
                <ListItem key={`item-${sectionId}-${item}`}>
                  <ListItemText primary={`${item}`} />
                  <ListItemText secondary={id} />
                </ListItem>
              ))}
            </ul>
          </li>
        ))}
      </List>
      <Stack direction="row" spacing={2}>
        <Button variant="outlined" startIcon={<DeleteIcon />}>
          Cancel
        </Button>
        <Button variant="contained" endIcon={<SendIcon />}>
          Save
        </Button>
      </Stack>
    </div>
  );
};
