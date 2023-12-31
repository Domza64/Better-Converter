import { FormEvent, useState } from "react";
import { getID } from "../utils/Utils";
import axios, { GET_VIDEO } from "../constants/Api";
import "./converter-form.css";

interface Props {
  convertFinished: (url: string, filename: string) => void;
  convertFailed: () => void;
  startConverting: () => void;
}

function ConvertForm({
  startConverting,
  convertFailed,
  convertFinished,
}: Props) {
  const [videoLink, setVideoLink] = useState("");
  const [checkbox3, setCheckbox3] = useState(true);
  const [checkbox4, setCheckbox4] = useState(false);

  const handleCheckboxChange = (checkbox: "mp3" | "mp4") => {
    if (checkbox === "mp3") {
      setCheckbox3(true);
      setCheckbox4(false);
    } else if (checkbox === "mp4") {
      setCheckbox3(false);
      setCheckbox4(true);
    }
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    startConverting();

    const id = getID(videoLink);
    const selectedType = checkbox3 ? "mp3" : "mp4";

    try {
      const response = await axios.get(
        `${GET_VIDEO}?id=${id}&type=${selectedType}`,
        {
          responseType: "blob",
        }
      );

      if (response.status === 200) {
        const filename = response.headers["x-filename"];
        convertFinished(
          window.URL.createObjectURL(new Blob([response.data])),
          filename
        );
      } else {
        convertFailed();
      }
    } catch {
      convertFailed();
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label className="link-input">
          Paste link to video here
          <input
            type="text"
            value={videoLink}
            required
            onChange={(e) => setVideoLink(e.target.value)}
          />
        </label>

        <div className="format-selection">
          <label>
            mp3
            <input
              type="checkbox"
              checked={checkbox3}
              onChange={() => handleCheckboxChange("mp3")}
            />
          </label>

          <label>
            mp4
            <input
              type="checkbox"
              checked={checkbox4}
              onChange={() => handleCheckboxChange("mp4")}
            />
          </label>
        </div>

        <button type="submit">Convert</button>
      </form>
    </div>
  );
}

export default ConvertForm;
