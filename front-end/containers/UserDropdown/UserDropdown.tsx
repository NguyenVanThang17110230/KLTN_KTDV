import React, { FC } from "react";
import Image from "next/image";

const ItemForm = ({ title, svg }: { title: string; svg: any }) => {
  return (
    <a
      href="#pablo"
      className={
        "text-sm py-2 px-4 font-normal w-60 whitespace-nowrap bg-transparent text-blueGray-700 flex items-center hover:bg-black hover:bg-opacity-10 group"
      }
      onClick={(e) => e.preventDefault()}
    >
      <div className="p-1 bg-gradient-to-r from-green-400 to-blue-500 rounded-md mr-2 group-hover:from-pink-500 group-hover:to-yellow-500">
        {svg}
      </div>
      {title}
    </a>
  );
};

const UserDropdown: FC = () => {
  // dropdown props
  const [dropdownPopoverShow, setDropdownPopoverShow] = React.useState(false);
  const openDropdownPopover = () => {
    setDropdownPopoverShow(true);
  };
  const closeDropdownPopover = () => {
    setDropdownPopoverShow(false);
  };
  return (
    <>
      <a
        className="text-blueGray-500 block relative"
        href="#pablo"
        onClick={(e) => {
          e.preventDefault();
          dropdownPopoverShow ? closeDropdownPopover() : openDropdownPopover();
        }}
      >
        <div className="items-center flex">
          <span className="w-12 h-12 text-sm text-white bg-blueGray-200 inline-flex items-center justify-center rounded-full overflow-hidden bg-gradient-to-r from-green-400 to-blue-500">
            <Image
              alt="..."
              className="w-full align-middle rounded-full "
              src="/static/img/avt.jpg"
              width={44}
              height={44}
            />
          </span>
        </div>
      </a>
      <div
        className={
          (dropdownPopoverShow ? "block " : "hidden ") +
          "bg-white text-base z-50 float-left py-2 list-none text-left rounded shadow-lg min-w-48 absolute top-14 right-0 transition-all"
        }
      >
        <ItemForm
          title="Profile"
          svg={
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="icon icon-tabler icon-tabler-user"
              width={20}
              height={20}
              viewBox="0 0 24 24"
              strokeWidth="2"
              stroke="#ffffff"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none" />
              <circle cx={12} cy={7} r={4} />
              <path d="M6 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2" />
            </svg>
          }
        />
        <ItemForm
          title="Settings"
          svg={
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="icon icon-tabler icon-tabler-settings"
              width={20}
              height={20}
              viewBox="0 0 24 24"
              strokeWidth={2}
              stroke="#ffffff"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none" />
              <path d="M10.325 4.317c.426 -1.756 2.924 -1.756 3.35 0a1.724 1.724 0 0 0 2.573 1.066c1.543 -.94 3.31 .826 2.37 2.37a1.724 1.724 0 0 0 1.065 2.572c1.756 .426 1.756 2.924 0 3.35a1.724 1.724 0 0 0 -1.066 2.573c.94 1.543 -.826 3.31 -2.37 2.37a1.724 1.724 0 0 0 -2.572 1.065c-.426 1.756 -2.924 1.756 -3.35 0a1.724 1.724 0 0 0 -2.573 -1.066c-1.543 .94 -3.31 -.826 -2.37 -2.37a1.724 1.724 0 0 0 -1.065 -2.572c-1.756 -.426 -1.756 -2.924 0 -3.35a1.724 1.724 0 0 0 1.066 -2.573c-.94 -1.543 .826 -3.31 2.37 -2.37c1 .608 2.296 .07 2.572 -1.065z" />
              <circle cx={12} cy={12} r={3} />
            </svg>
          }
        />
        <div className="h-0 my-2 border border-solid border-blueGray-100" />
        <ItemForm
          title="Sign out"
          svg={
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="icon icon-tabler icon-tabler-logout"
              width={20}
              height={20}
              viewBox="0 0 24 24"
              strokeWidth={2}
              stroke="#ffffff"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none" />
              <path d="M14 8v-2a2 2 0 0 0 -2 -2h-7a2 2 0 0 0 -2 2v12a2 2 0 0 0 2 2h7a2 2 0 0 0 2 -2v-2" />
              <path d="M7 12h14l-3 -3m0 6l3 -3" />
            </svg>
          }
        />
      </div>
    </>
  );
};

export default UserDropdown;
